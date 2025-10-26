package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class DoctorRequest {
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String specialization;
    private String qualification;
    private String contactNumber;
    private String email;
    private Long departmentId;
    private Integer experienceYears;
    private Double consultationFee;
}
