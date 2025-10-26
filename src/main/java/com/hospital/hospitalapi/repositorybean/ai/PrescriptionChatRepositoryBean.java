package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.entity.ai.PrescriptionQuery;
import com.hospital.hospitalapi.repository.ai.PrescriptionQueryRepository;
import com.hospital.hospitalapi.exception.PrescriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionChatRepositoryBean {
    
    private final PrescriptionQueryRepository prescriptionQueryRepository;
    private final PrescriptionAnalysisRepositoryBean prescriptionAnalysisRepositoryBean;
    private final GeminiIntegrationRepositoryBean geminiIntegrationRepositoryBean;
    
    @Transactional
    public PrescriptionQuery askQuestion(Long prescriptionImageId, String question) {
        log.info("Processing question for prescription image ID: {}", prescriptionImageId);
        
        PrescriptionImage prescriptionImage = 
            prescriptionAnalysisRepositoryBean.getPrescriptionImageById(prescriptionImageId);
        
        // Get AI response
        String answer = geminiIntegrationRepositoryBean.answerPrescriptionQuestion(
            question, 
            prescriptionImage.getAiAnalysisReport()
        );
        
        // Save query
        PrescriptionQuery query = new PrescriptionQuery();
        query.setPrescriptionImage(prescriptionImage);
        query.setPatient(prescriptionImage.getPatient());
        query.setUserQuestion(question);
        query.setAiResponse(answer);
        
        PrescriptionQuery saved = prescriptionQueryRepository.save(query);
        log.info("Query saved with ID: {}", saved.getId());
        return saved;
    }
    
    public List<PrescriptionQuery> getChatHistory(Long prescriptionImageId) {
        PrescriptionImage prescriptionImage = 
            prescriptionAnalysisRepositoryBean.getPrescriptionImageById(prescriptionImageId);
        return prescriptionQueryRepository.findByPrescriptionImageOrderByAskedAtAsc(prescriptionImage);
    }
}
