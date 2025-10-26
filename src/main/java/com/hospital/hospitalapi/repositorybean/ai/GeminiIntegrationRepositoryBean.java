package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.exception.ai.AIServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
public class GeminiIntegrationRepositoryBean {
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public String analyzePrescription(String imageUrl) {
        log.info("Calling Gemini API to analyze prescription");
        
        try {
            // Prepare request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + geminiApiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("image_url", imageUrl);
            requestBody.put("prompt", "Analyze this prescription image and extract: " +
                "medicine names, dosages, frequency, duration, and any special instructions. " +
                "Format the response as JSON.");
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Call API
            ResponseEntity<String> response = restTemplate.exchange(
                geminiApiUrl + "/analyze-image",
                HttpMethod.POST,
                request,
                String.class
            );
            
            log.info("Gemini API call successful");
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            throw new AIServiceException("Failed to analyze prescription with Gemini AI", e);
        }
    }
    
    public String answerPrescriptionQuestion(String question, String prescriptionContext) {
        log.info("Calling Gemini API to answer question");
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + geminiApiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("question", question);
            requestBody.put("context", prescriptionContext);
            requestBody.put("prompt", "Answer the following question about the prescription: " + question);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                geminiApiUrl + "/chat",
                HttpMethod.POST,
                request,
                String.class
            );
            
            log.info("Gemini chat API call successful");
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Gemini chat API call failed", e);
            throw new AIServiceException("Failed to get answer from Gemini AI", e);
        }
    }
    
    public String getMedicineInfo(String medicineName) {
        log.info("Calling Gemini API for medicine info: {}", medicineName);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + geminiApiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("medicine_name", medicineName);
            requestBody.put("prompt", "Provide detailed information about the medicine: " + 
                medicineName + ". Include uses, dosage, side effects, precautions, interactions.");
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                geminiApiUrl + "/medicine-info",
                HttpMethod.POST,
                request,
                String.class
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Gemini medicine info API call failed", e);
            throw new AIServiceException("Failed to get medicine info from Gemini AI", e);
        }
    }
}
