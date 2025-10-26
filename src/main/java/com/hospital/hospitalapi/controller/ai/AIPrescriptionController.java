package com.hospital.hospitalapi.controller.ai;

import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.entity.ai.PrescriptionQuery;
import com.hospital.hospitalapi.dto.request.ai.PrescriptionQuestionRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.ai.*;
import com.hospital.hospitalapi.repositorybean.ai.PrescriptionAnalysisRepositoryBean;
import com.hospital.hospitalapi.repositorybean.ai.PrescriptionChatRepositoryBean;
import com.hospital.hospitalapi.repositorybean.ai.GeminiIntegrationRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai/prescriptions")
@RequiredArgsConstructor
@Tag(name = "AI Prescription Intelligence", description = "AI-powered prescription analysis and Q&A")
public class AIPrescriptionController {
    
    private final PrescriptionAnalysisRepositoryBean prescriptionAnalysisRepositoryBean;
    private final PrescriptionChatRepositoryBean prescriptionChatRepositoryBean;
    private final GeminiIntegrationRepositoryBean geminiIntegrationRepositoryBean;
    
    @PostMapping("/{prescriptionId}/upload-image")
    @Operation(summary = "Upload prescription image for AI analysis")
    public ResponseEntity<ApiResponse<PrescriptionAnalysisResponse>> uploadPrescriptionImage(
            @PathVariable Long prescriptionId,
            @RequestParam("file") MultipartFile file) {
        
        PrescriptionImage image = prescriptionAnalysisRepositoryBean.uploadPrescriptionImage(prescriptionId, file);
        
        PrescriptionAnalysisResponse response = new PrescriptionAnalysisResponse();
        response.setImageId(image.getId());
        response.setStatus(image.getAnalysisStatus());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Prescription image uploaded. Analysis in progress.", response));
    }
    
    @GetMapping("/images/{imageId}")
    @Operation(summary = "Get prescription image details")
    public ResponseEntity<ApiResponse<PrescriptionAnalysisResponse>> getPrescriptionImage(@PathVariable Long imageId) {
        PrescriptionImage image = prescriptionAnalysisRepositoryBean.getPrescriptionImageById(imageId);
        
        PrescriptionAnalysisResponse response = new PrescriptionAnalysisResponse();
        response.setImageId(image.getId());
        response.setStatus(image.getAnalysisStatus());
        response.setExtractedText(image.getExtractedText());
        response.setConfidenceScore(image.getConfidenceScore());
        response.setAnalyzedAt(image.getAnalyzedAt());
        
        return ResponseEntity.ok(ApiResponse.success("Prescription image retrieved", response));
    }
    
    @GetMapping("/images/{imageId}/analysis")
    @Operation(summary = "Get AI analysis report")
    public ResponseEntity<ApiResponse<PrescriptionAnalysisResponse>> getAnalysis(@PathVariable Long imageId) {
        PrescriptionImage image = prescriptionAnalysisRepositoryBean.getPrescriptionImageById(imageId);
        
        if (!"COMPLETED".equals(image.getAnalysisStatus())) {
            return ResponseEntity.ok(ApiResponse.error("Analysis not completed yet"));
        }
        
        PrescriptionAnalysisResponse response = new PrescriptionAnalysisResponse();
        response.setImageId(image.getId());
        response.setStatus(image.getAnalysisStatus());
        response.setExtractedText(image.getExtractedText());
        response.setConfidenceScore(image.getConfidenceScore());
        response.setAnalyzedAt(image.getAnalyzedAt());
        
        return ResponseEntity.ok(ApiResponse.success("Analysis report retrieved", response));
    }
    
    @PostMapping("/images/{imageId}/reanalyze")
    @Operation(summary = "Trigger re-analysis")
    public ResponseEntity<ApiResponse<Void>> reanalyze(@PathVariable Long imageId) {
        prescriptionAnalysisRepositoryBean.analyzeImageAsync(imageId);
        return ResponseEntity.ok(ApiResponse.success("Re-analysis triggered", null));
    }
    
    @PostMapping("/images/{imageId}/ask")
    @Operation(summary = "Ask question about prescription")
    public ResponseEntity<ApiResponse<PrescriptionAnswerResponse>> askQuestion(
            @PathVariable Long imageId,
            @RequestBody PrescriptionQuestionRequest request) {
        
        PrescriptionQuery query = prescriptionChatRepositoryBean.askQuestion(imageId, request.getQuestion());
        
        PrescriptionAnswerResponse response = new PrescriptionAnswerResponse();
        response.setQuestion(query.getUserQuestion());
        response.setAnswer(query.getAiResponse());
        response.setAskedAt(query.getAskedAt());
        
        return ResponseEntity.ok(ApiResponse.success("Question answered", response));
    }
    
    @GetMapping("/images/{imageId}/chat-history")
    @Operation(summary = "Get chat history")
    public ResponseEntity<ApiResponse<ChatHistoryResponse>> getChatHistory(@PathVariable Long imageId) {
        List<PrescriptionQuery> queries = prescriptionChatRepositoryBean.getChatHistory(imageId);
        
        ChatHistoryResponse response = new ChatHistoryResponse();
        response.setPrescriptionImageId(imageId);
        
        List<ChatHistoryResponse.ChatMessage> messages = queries.stream()
            .map(q -> {
                ChatHistoryResponse.ChatMessage msg = new ChatHistoryResponse.ChatMessage();
                msg.setQuestion(q.getUserQuestion());
                msg.setAnswer(q.getAiResponse());
                msg.setAskedAt(q.getAskedAt().toString());
                return msg;
            })
            .collect(Collectors.toList());
        
        response.setChatHistory(messages);
        
        return ResponseEntity.ok(ApiResponse.success("Chat history retrieved", response));
    }
    
    @GetMapping("/medicines/info")
    @Operation(summary = "Get medicine information")
    public ResponseEntity<ApiResponse<String>> getMedicineInfo(@RequestParam String name) {
        String info = geminiIntegrationRepositoryBean.getMedicineInfo(name);
        return ResponseEntity.ok(ApiResponse.success("Medicine info retrieved", info));
    }
    
    @GetMapping("/patient/{patientId}/all-analyzed")
    @Operation(summary = "Get all analyzed prescriptions for patient")
    public ResponseEntity<ApiResponse<List<PrescriptionAnalysisResponse>>> getPatientAnalyzedPrescriptions(
            @PathVariable Long patientId) {
        
        List<PrescriptionImage> images = prescriptionAnalysisRepositoryBean.getPatientAnalyzedPrescriptions(patientId);
        
        List<PrescriptionAnalysisResponse> responses = images.stream()
            .map(img -> {
                PrescriptionAnalysisResponse resp = new PrescriptionAnalysisResponse();
                resp.setImageId(img.getId());
                resp.setStatus(img.getAnalysisStatus());
                resp.setConfidenceScore(img.getConfidenceScore());
                resp.setAnalyzedAt(img.getAnalyzedAt());
                return resp;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success("Patient prescriptions retrieved", responses));
    }
}
