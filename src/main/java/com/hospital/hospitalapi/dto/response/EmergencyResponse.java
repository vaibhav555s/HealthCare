package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmergencyResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime arrivalTime;
    private String severity;
    private String chiefComplaint;
    private String status;
    private LocalDateTime treatmentStartTime;
}