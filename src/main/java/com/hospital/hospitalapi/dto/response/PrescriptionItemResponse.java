package com.hospital.hospitalapi.dto.response;

import lombok.Data;

@Data
public class PrescriptionItemResponse {
    private Long id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private Integer quantity;
    private String instructions;
}
