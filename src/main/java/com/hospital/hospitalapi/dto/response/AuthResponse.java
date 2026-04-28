package com.hospital.hospitalapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long patientId;  // kept for backward compatibility
    private String role;     // "PATIENT" or "DOCTOR"
    private Long userId;     // generic user id (patient or doctor id)

    // backward-compatible constructor
    public AuthResponse(String token, Long patientId) {
        this.token = token;
        this.patientId = patientId;
        this.role = "PATIENT";
        this.userId = patientId;
    }
}
