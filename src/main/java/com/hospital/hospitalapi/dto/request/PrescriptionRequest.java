package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PrescriptionRequest {
    private Long patientId;
    private Long doctorId;
    private Long medicalHistoryId;
    private LocalDateTime prescriptionDate;
    private String diagnosis;
    private String instructions;
    private String followUpInstructions;
}