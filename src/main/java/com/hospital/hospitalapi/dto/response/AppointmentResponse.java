package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String doctorName;
    private String departmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String tokenNumber;
    private String status;
    private String reason;
    private String notes;
    private LocalDateTime createdAt;
    
    // Consultation data (filled after doctor completes)
    private String diagnosis;
    private String medicines;
    private String consultationNotes;
    
    // Patient feedback
    private Integer rating;
    private String feedbackComment;
}
