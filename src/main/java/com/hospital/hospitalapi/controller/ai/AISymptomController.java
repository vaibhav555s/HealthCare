package com.hospital.hospitalapi.controller.ai;

import com.hospital.hospitalapi.dto.request.SymptomCheckRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.repositorybean.ai.GeminiIntegrationRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Healthcare Intelligence")
public class AISymptomController {

    private final GeminiIntegrationRepositoryBean geminiService;

    @PostMapping("/symptom-check")
    @Operation(summary = "AI Symptom Checker — suggests department and urgency based on symptoms")
    public ResponseEntity<ApiResponse<String>> checkSymptoms(@RequestBody SymptomCheckRequest request) {
        String result = geminiService.checkSymptoms(request.getSymptoms());
        return ResponseEntity.ok(ApiResponse.success("Symptom analysis complete", result));
    }
}
