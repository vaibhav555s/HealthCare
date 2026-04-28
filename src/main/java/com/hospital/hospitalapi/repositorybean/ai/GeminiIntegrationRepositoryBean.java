package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.exception.ai.AIServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.file.Path;
import java.net.URI;
import java.util.*;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class GeminiIntegrationRepositoryBean {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    // e.g. https://generativelanguage.googleapis.com/v1beta/models/gemini-3.0-flash
    private String geminiApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Analyze a prescription image by fetching the image bytes (from imageUrl),
     * base64 encoding and sending to Gemini generateContent endpoint.
     */
    public String analyzePrescription(String imageUrl) {
        log.info("Calling Gemini generateContent to analyze prescription image: {}", imageUrl);

        try {
            // 1) download image bytes
            byte[] imageBytes = downloadImageBytes(imageUrl);
            if (imageBytes == null || imageBytes.length == 0) {
                throw new AIServiceException("Could not download image or image is empty: " + imageUrl);
            }

            // detect mime type (basic heuristic)
            String mimeType = guessMimeType(imageUrl);
            if (mimeType == null)
                mimeType = "image/jpeg";

            // 2) build contents array:
            // contents: [
            // { parts: [ { inline_data: { mime_type, data } }, { text: "Your prompt..." } ]
            // }
            // ]
            String imageB64 = Base64.getEncoder().encodeToString(imageBytes);

            Map<String, Object> inlineDataPart = new HashMap<>();
            inlineDataPart.put("inline_data", Map.of("mime_type", mimeType, "data", imageB64));

            Map<String, Object> textPart = new HashMap<>();
            String prompt = "Analyze this prescription image and extract medicine names, dosages, frequency, duration. Give description about what the medicine is and why am i taking it and how does it cure my specified issue.";
            textPart.put("text", prompt);

            List<Object> parts = new ArrayList<>();
            parts.add(inlineDataPart);
            parts.add(textPart);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", parts);

            Map<String, Object> body = new HashMap<>();
            body.put("contents", List.of(content));

            String url = geminiApiUrl + ":generateContent";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);
            HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            log.info("Gemini generateContent (image) call successful: status={}", response.getStatusCodeValue());
            return extractTextFromGeminiResponse(response.getBody());

        } catch (HttpClientErrorException | ResourceAccessException e) {
            log.error("Gemini API HTTP/Network error", e);
            throw new AIServiceException("Failed to analyze prescription with Gemini AI: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("JSON serialization error", e);
            throw new AIServiceException("Failed to build request for Gemini AI", e);
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            throw new AIServiceException("Failed to analyze prescription with Gemini AI", e);
        }
    }

    /**
     * Answer a question about a prescription using the context (string).
     * Uses a text-only generateContent request.
     */
    public String answerPrescriptionQuestion(String question, String prescriptionContext) {
        log.info("Calling Gemini generateContent to answer question");

        try {
            String prompt = "You are a helpful assistant. Given the prescription context below, answer the question.\n\nContext:\n"
                    + prescriptionContext + "\n\nQuestion:\n" + question
                    + "\n\nAnswer concisely.";

            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(textPart));
            Map<String, Object> body = Map.of("contents", List.of(content));

            String url = geminiApiUrl + ":generateContent";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);
            HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("Gemini generateContent (text) call successful: status={}", response.getStatusCodeValue());
            return extractTextFromGeminiResponse(response.getBody());

        } catch (Exception e) {
            log.error("Gemini chat API call failed", e);
            throw new AIServiceException("Failed to get answer from Gemini AI", e);
        }
    }

    /**
     * Get medicine info by asking Gemini (text-only).
     */
    public String getMedicineInfo(String medicineName) {
        log.info("Calling Gemini generateContent for medicine info: {}", medicineName);

        try {
            String prompt = "Provide detailed information about the medicine: " + medicineName +
                    ". Include uses, typical dosages, common side effects, precautions, and major interactions. " +
                    "Format the response as a readable text with sections for Uses, Dosages, Side Effects, Precautions, and Interactions. " +
                    "Do NOT wrap it in JSON or code blocks.";

            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(textPart));
            Map<String, Object> body = Map.of("contents", List.of(content));

            String url = geminiApiUrl + ":generateContent";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);
            HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return extractTextFromGeminiResponse(response.getBody());
        } catch (Exception e) {
            log.error("Gemini medicine info API call failed", e);
            throw new AIServiceException("Failed to get medicine info from Gemini AI", e);
        }
    }

    /**
     * AI Symptom Checker — analyzes patient symptoms and suggests
     * department, urgency level, and preliminary advice.
     */
    public String checkSymptoms(String symptoms) {
        log.info("Calling Gemini for symptom analysis");

        try {
            String prompt = "You are an AI healthcare assistant. A patient describes the following symptoms:\n\n\"" 
                + symptoms + "\"\n\n"
                + "Based on these symptoms, provide:\n"
                + "1. **Recommended Department**: Which hospital department they should visit (e.g., Cardiology, Neurology, Orthopedics, Dermatology, Pediatrics, General Medicine)\n"
                + "2. **Urgency Level**: Low, Medium, High, or Emergency\n"
                + "3. **Possible Conditions**: List 2-3 possible conditions (not a diagnosis)\n"
                + "4. **Immediate Advice**: What the patient should do right now\n"
                + "5. **Questions to Expect**: What the doctor might ask them\n\n"
                + "Format the response clearly with these sections. Be empathetic but clear. "
                + "Add a disclaimer that this is AI-assisted guidance, not a medical diagnosis.";

            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(textPart));
            Map<String, Object> body = Map.of("contents", List.of(content));

            String url = geminiApiUrl + ":generateContent";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);
            HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("Gemini symptom check call successful");
            return extractTextFromGeminiResponse(response.getBody());
        } catch (Exception e) {
            log.error("Gemini symptom check failed", e);
            throw new AIServiceException("Failed to analyze symptoms with AI", e);
        }
    }

    // ---------------- helper methods ----------------

    /**
     * Extract the text content from a Gemini generateContent response.
     * The response structure is: { candidates: [{ content: { parts: [{ text: "..." }] } }] }
     */
    @SuppressWarnings("unchecked")
    private String extractTextFromGeminiResponse(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "No response from AI.";
        }
        try {
            Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> firstCandidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                if (content != null) {
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        if (text != null) {
                            // Strip markdown code block wrappers if present
                            text = text.trim();
                            if (text.startsWith("```json")) {
                                text = text.substring(7);
                            } else if (text.startsWith("```")) {
                                text = text.substring(3);
                            }
                            if (text.endsWith("```")) {
                                text = text.substring(0, text.length() - 3);
                            }
                            return text.trim();
                        }
                    }
                }
            }
            log.warn("Could not extract text from Gemini response: {}", responseBody);
            return "AI response could not be parsed.";
        } catch (Exception e) {
            log.error("Failed to parse Gemini response JSON", e);
            return responseBody; // fallback to raw response
        }
    }

    private byte[] downloadImageBytes(String imageUrl) {
        try {
            // Normalize backslashes in Windows-style paths
            imageUrl = imageUrl.replace("\\", "/");

            // Case 1: if it's a local file path (no http/https/file prefix)
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://") && !imageUrl.startsWith("file:/")) {
                Path path = Paths.get(imageUrl);
                if (!Files.exists(path)) {
                    throw new IOException("Image not found at: " + path.toAbsolutePath());
                }
                log.info("Reading local file: {}", path.toAbsolutePath());
                return Files.readAllBytes(path);
            }

            // Case 2: if it’s a remote URL
            log.info("Downloading image from URL: {}", imageUrl);
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Java/RestTemplate");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(15_000);
            conn.setRequestMethod("GET");
            conn.connect();

            try (InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                return baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("Failed to load image: {}", imageUrl, e);
            return null;
        }
    }

    private String guessMimeType(String imageUrl) {
        if (imageUrl == null)
            return null;
        String lower = imageUrl.toLowerCase();
        if (lower.endsWith(".png"))
            return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))
            return "image/jpeg";
        if (lower.endsWith(".webp"))
            return "image/webp";
        if (lower.endsWith(".heic"))
            return "image/heic";
        if (lower.endsWith(".heif"))
            return "image/heif";
        return null;
    }
}
