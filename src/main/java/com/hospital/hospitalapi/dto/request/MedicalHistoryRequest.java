package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalHistoryRequest {
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
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
