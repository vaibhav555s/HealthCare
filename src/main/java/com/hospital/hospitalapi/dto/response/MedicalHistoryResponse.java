package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalHistoryResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime visitDate;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String bloodPressure;
    private Double temperature;
    private Integer pulse;
    private Double weight;
    private Double height;
    private String notes;
}
