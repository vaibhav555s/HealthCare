package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String contactNumber;
    private String email;
    private String address;
    private String bloodGroup;
    private String allergies;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String password;
}
