package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.entity.Prescription;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.exception.PrescriptionNotFoundException;
import com.hospital.hospitalapi.exception.ai.InvalidFileException;
import com.hospital.hospitalapi.exception.ai.ImageAnalysisFailedException;
import com.hospital.hospitalapi.repository.ai.PrescriptionImageRepository;
import com.hospital.hospitalapi.repositorybean.PatientRepositoryBean;
import com.hospital.hospitalapi.repositorybean.PrescriptionRepositoryBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionAnalysisRepositoryBean {

    private final PrescriptionImageRepository prescriptionImageRepository;
    private final PrescriptionRepositoryBean prescriptionRepositoryBean;
    private final PatientRepositoryBean patientRepositoryBean;
    private final GeminiIntegrationRepositoryBean geminiIntegrationRepositoryBean;
    private final FileStorageRepositoryBean fileStorageRepositoryBean;

    @Transactional
    public PrescriptionImage uploadPrescriptionImage(Long prescriptionId, MultipartFile file) {
        log.info("Uploading prescription image for prescription ID: {}", prescriptionId);

        validateFile(file);

        Prescription prescription = prescriptionRepositoryBean.getPrescriptionById(prescriptionId);

        String imageUrl = fileStorageRepositoryBean.storeFile(file);

        PrescriptionImage prescriptionImage = new PrescriptionImage();
        prescriptionImage.setPrescription(prescription);
        prescriptionImage.setPatient(prescription.getPatient());
        prescriptionImage.setImageUrl(imageUrl);
        prescriptionImage.setImageFileName(file.getOriginalFilename());
        prescriptionImage.setAnalysisStatus("PENDING");

        PrescriptionImage saved = prescriptionImageRepository.save(prescriptionImage);
        log.info("Prescription image uploaded with ID: {}", saved.getId());

        // FIXED: Call analysis and update the same entity object
        try {
            saved = analyzeImageAndReturn(saved);
            log.info("Analysis completed successfully for image ID: {}", saved.getId());
        } catch (Exception e) {
            log.error("Analysis failed for image ID: {}", saved.getId(), e);
            saved.setAnalysisStatus("FAILED");
            saved.setAiAnalysisReport("Error: " + e.getMessage());
            saved = prescriptionImageRepository.save(saved);
        }

        return saved;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) throw new InvalidFileException("File is empty");

        String contentType = file.getContentType();
        if (contentType == null ||
            (!contentType.equals("image/jpeg") &&
             !contentType.equals("image/png") &&
             !contentType.equals("application/pdf"))) {
            throw new InvalidFileException("Only JPG, PNG, and PDF files are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024)
            throw new InvalidFileException("File size must be less than 5MB");
    }

    private PrescriptionImage analyzeImageAndReturn(PrescriptionImage image) {
        log.info("Starting analysis for prescription image ID: {}", image.getId());

        try {
            image.setAnalysisStatus("ANALYZING");
            image = prescriptionImageRepository.save(image);

            String rawResponse = geminiIntegrationRepositoryBean.analyzePrescription(image.getImageUrl());
            log.debug("Gemini raw response: {}", rawResponse);

            String extractedText = null;
            String analyzedMedicines = null;

            try {
                var json = new ObjectMapper().readTree(rawResponse);
                extractedText = json.path("candidates")
                                    .path(0)
                                    .path("content")
                                    .path("parts")
                                    .path(0)
                                    .path("text")
                                    .asText();

                analyzedMedicines = extractedText
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();

                log.info("Extracted text: {}", extractedText);
                log.info("Analyzed medicines JSON: {}", analyzedMedicines);

            } catch (Exception parseEx) {
                log.error("Failed to parse Gemini response", parseEx);
                extractedText = rawResponse;
                analyzedMedicines = null;
            }

            image.setAiAnalysisReport(rawResponse);
            image.setExtractedText(extractedText);
            image.setAnalyzedMedicines(analyzedMedicines);
            image.setAnalysisStatus("COMPLETED");
            image.setConfidenceScore(95);
            image.setAnalyzedAt(LocalDateTime.now());

            image = prescriptionImageRepository.save(image);

            log.info("Analysis completed for prescription image ID: {}", image.getId());
            return image;

        } catch (Exception e) {
            log.error("Analysis failed for prescription image ID: {}", image.getId(), e);
            
            image.setAnalysisStatus("FAILED");
            image.setAiAnalysisReport("Error: " + e.getMessage());
            image.setAnalyzedAt(LocalDateTime.now());
            image = prescriptionImageRepository.save(image);
            
            throw new ImageAnalysisFailedException("Failed to analyze prescription image", e);
        }
    }

    @Transactional
    public void analyzeImageAsync(Long imageId) {
        log.info("Starting async analysis for prescription image ID: {}", imageId);

        try {
            PrescriptionImage image = getPrescriptionImageById(imageId);
            analyzeImageAndReturn(image);
        } catch (Exception e) {
            log.error("Analysis failed for prescription image ID: {}", imageId, e);
            PrescriptionImage image = getPrescriptionImageById(imageId);
            image.setAnalysisStatus("FAILED");
            image.setAiAnalysisReport("Error: " + e.getMessage());
            prescriptionImageRepository.save(image);
            throw new ImageAnalysisFailedException("Failed to analyze prescription image", e);
        }
    }

    public PrescriptionImage getPrescriptionImageById(Long id) {
        return prescriptionImageRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription image not found with ID: " + id));
    }

    public List<PrescriptionImage> getPatientAnalyzedPrescriptions(Long patientId) {
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        return prescriptionImageRepository.findByPatientOrderByUploadedAtDesc(patient);
    }
    
    
}