package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.dto.request.LoginRequest;
import com.hospital.hospitalapi.dto.response.AuthResponse;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Auto-detect role: try patient first, then doctor
        Optional<Patient> patientOpt = patientRepository.findByEmail(request.getEmail());
        if (patientOpt.isPresent()) {
            return loginPatient(patientOpt.get(), request.getPassword());
        }

        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(request.getEmail());
        if (doctorOpt.isPresent()) {
            return loginDoctor(doctorOpt.get(), request.getPassword());
        }

        throw new RuntimeException("No account found with this email address");
    }

    private ResponseEntity<AuthResponse> loginPatient(Patient patient, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, patient.getPassword())) {
            throw new RuntimeException("Incorrect password. Please try again.");
        }

        String token = jwtUtil.generateToken(patient.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, patient.getId(), "PATIENT", patient.getId()));
    }

    private ResponseEntity<AuthResponse> loginDoctor(Doctor doctor, String rawPassword) {
        if (doctor.getPassword() == null) {
            throw new RuntimeException("Doctor account not configured. Please contact administration.");
        }
        if (!passwordEncoder.matches(rawPassword, doctor.getPassword())) {
            throw new RuntimeException("Incorrect password. Please try again.");
        }
        if (doctor.getIsActive() != null && !doctor.getIsActive()) {
            throw new RuntimeException("This account has been deactivated. Please contact administration.");
        }

        String token = jwtUtil.generateToken(doctor.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, null, "DOCTOR", doctor.getId()));
    }
}
