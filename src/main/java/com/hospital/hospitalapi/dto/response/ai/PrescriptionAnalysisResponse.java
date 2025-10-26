package com.hospital.hospitalapi.dto.response.ai;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionAnalysisResponse {
    private Long imageId;
    private String status;
    private String extractedText;
    private List<AnalyzedMedicine> analyzedMedicines;
    private List<String> warnings;
    private Integer confidenceScore;
    private LocalDateTime analyzedAt;
    
    @Data
    public static class AnalyzedMedicine {
        private String medicineName;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;
    }
}
