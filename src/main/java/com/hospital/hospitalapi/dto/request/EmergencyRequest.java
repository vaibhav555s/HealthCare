package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmergencyRequest {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime arrivalTime;
    private String severity;
    private String chiefComplaint;
    private String initialAssessment;
}