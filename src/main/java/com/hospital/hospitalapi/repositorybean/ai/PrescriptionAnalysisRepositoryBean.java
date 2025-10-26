package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.entity.Prescription;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.repository.ai.PrescriptionImageRepository;
import com.hospital.hospitalapi.repositorybean.PrescriptionRepositoryBean;
import com.hospital.hospitalapi.repositorybean.PatientRepositoryBean;
import com.hospital.hospitalapi.exception.PrescriptionNotFoundException;
import com.hospital.hospitalapi.exception.ai.InvalidFileException;
import com.hospital.hospitalapi.exception.ai.ImageAnalysisFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

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
        
        // Validate file
        validateFile(file);
        
        Prescription prescription = prescriptionRepositoryBean.getPrescriptionById(prescriptionId);
        
        // Store file
        String imageUrl = fileStorageRepositoryBean.storeFile(file);
        
        // Create prescription image record
        PrescriptionImage prescriptionImage = new PrescriptionImage();
        prescriptionImage.setPrescription(prescription);
        prescriptionImage.setPatient(prescription.getPatient());
        prescriptionImage.setImageUrl(imageUrl);
        prescriptionImage.setImageFileName(file.getOriginalFilename());
        prescriptionImage.setAnalysisStatus("PENDING");
        
        PrescriptionImage saved = prescriptionImageRepository.save(prescriptionImage);
        log.info("Prescription image uploaded with ID: {}", saved.getId());
        
        // Trigger async analysis
        analyzeImageAsync(saved.getId());
        
        return saved;
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || 
            (!contentType.equals("image/jpeg") && 
             !contentType.equals("image/png") && 
             !contentType.equals("application/pdf"))) {
            throw new InvalidFileException("Only JPG, PNG, and PDF files are allowed");
        }
        
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new InvalidFileException("File size must be less than 5MB");
        }
    }
    
    @Transactional
    public void analyzeImageAsync(Long imageId) {
        log.info("Starting async analysis for prescription image ID: {}", imageId);
        
        try {
            PrescriptionImage image = getPrescriptionImageById(imageId);
            image.setAnalysisStatus("ANALYZING");
            prescriptionImageRepository.save(image);
            
            // Call Gemini API
            String analysisReport = geminiIntegrationRepositoryBean.analyzePrescription(image.getImageUrl());
            
            // Update with results
            image.setAiAnalysisReport(analysisReport);
            image.setAnalysisStatus("COMPLETED");
            image.setConfidenceScore(95);
            image.setAnalyzedAt(LocalDateTime.now());
            
            prescriptionImageRepository.save(image);
            log.info("Analysis completed for prescription image ID: {}", imageId);
            
        } catch (Exception e) {
            log.error("Analysis failed for prescription image ID: {}", imageId, e);
            PrescriptionImage image = getPrescriptionImageById(imageId);
            image.setAnalysisStatus("FAILED");
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
