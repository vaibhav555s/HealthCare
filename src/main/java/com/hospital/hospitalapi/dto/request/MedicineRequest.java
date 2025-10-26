package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicineRequest {
    private String name;
    private String genericName;
    private String category;
    private String manufacturer;
    private Double unitPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String description;
    private String sideEffects;
}