package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class ConsultationRequest {
    // Clinical data
    private String symptoms;
    private String diagnosis;
    private String treatment;
    
    // Vitals
    private String bloodPressure;
    private Double temperature;
    private Integer pulse;
    private Double weight;
    private Double height;
    
    // Prescription (free text — more practical than medicine DB lookup)
    private String medicines;        // e.g. "Paracetamol 500mg - 2x daily for 5 days\nAmoxicillin 250mg - 3x daily for 7 days"
    private String instructions;     // e.g. "Take after food. Drink plenty of fluids."
    private String followUpInstructions;  // e.g. "Follow up in 1 week if symptoms persist."
    
    // Follow-up
    private String followUpDate;     // Optional: auto-create follow-up appointment (ISO date)
    
    // Notes
    private String notes;
}
