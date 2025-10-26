package com.hospital.hospitalapi.dto.response.ai;

import lombok.Data;
import java.util.List;

@Data
public class MedicineInfoResponse {
    private String medicineName;
    private String genericName;
    private String category;
    private List<String> uses;
    private String dosage;
    private List<String> sideEffects;
    private List<String> precautions;
    private List<String> interactions;
    private String pregnancy;
    private String storage;
}
