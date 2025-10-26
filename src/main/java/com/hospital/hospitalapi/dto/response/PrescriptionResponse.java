package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime prescriptionDate;
    private String diagnosis;
    private String instructions;
    private String followUpInstructions;
    private List<PrescriptionItemResponse> items;
}
