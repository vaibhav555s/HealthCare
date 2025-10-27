package com.hospital.hospitalapi.dto.response.ai;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrescriptionAnalysisResponse {
    
    private Long imageId;
    private String status;
    private String extractedText;
    private String analyzedMedicines;
    private Integer confidenceScore;
    private LocalDateTime analyzedAt;
    private String statusMessage;
    
    public String getStatusMessage() {
        if (statusMessage != null) return statusMessage;
        
        return switch (status != null ? status : "") {
            case "PENDING" -> "Analysis is pending";
            case "ANALYZING" -> "Analysis in progress";
            case "COMPLETED" -> "Analysis completed successfully";
            case "FAILED" -> "Analysis failed";
            default -> "Unknown status";
        };
    }
}