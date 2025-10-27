package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String specialization;
    private String qualification;
    private String contactNumber;
    private String email;
    private String departmentName;
    private Integer experienceYears;
    private Double consultationFee;
    private Boolean isAvailable;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}