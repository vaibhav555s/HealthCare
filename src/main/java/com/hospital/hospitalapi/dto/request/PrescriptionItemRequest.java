package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class PrescriptionItemRequest {
    private Long prescriptionId;
    private Long medicineId;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private Integer quantity;
    private String instructions;
}