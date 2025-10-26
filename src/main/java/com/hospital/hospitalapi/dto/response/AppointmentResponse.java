package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
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
}
