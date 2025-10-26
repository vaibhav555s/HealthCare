package com.hospital.hospitalapi.dto.response.ai;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionAnswerResponse {
    private String question;
    private String answer;
    private List<String> relatedInfo;
    private LocalDateTime askedAt;
}