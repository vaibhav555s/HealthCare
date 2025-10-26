package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.dto.request.PatientRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.PatientResponse;
import com.hospital.hospitalapi.repositorybean.PatientRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {
    
    private final PatientRepositoryBean patientRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Register new patient")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@RequestBody PatientRequest request) {
        Patient patient = mapToEntity(request);
        Patient created = patientRepositoryBean.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Patient created successfully", mapToResponse(created)));
    }
    
    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        List<PatientResponse> patients = patientRepositoryBean.getAllPatients()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Patients retrieved successfully", patients));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepositoryBean.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved successfully", mapToResponse(patient)));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update patient")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id, @RequestBody PatientRequest request) {
        Patient patient = mapToEntity(request);
        Patient updated = patientRepositoryBean.updatePatient(id, patient);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", mapToResponse(updated)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientRepositoryBean.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully", null));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search patients by name")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> searchPatients(@RequestParam String query) {
        List<PatientResponse> patients = patientRepositoryBean.searchPatients(query)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search completed", patients));
    }
    
    private Patient mapToEntity(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setContactNumber(request.getContactNumber());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactNumber(request.getEmergencyContactNumber());
        return patient;
    }
    
    private PatientResponse mapToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setContactNumber(patient.getContactNumber());
        response.setEmail(patient.getEmail());
        response.setAddress(patient.getAddress());
        response.setBloodGroup(patient.getBloodGroup());
        response.setAllergies(patient.getAllergies());
        response.setEmergencyContactName(patient.getEmergencyContactName());
        response.setEmergencyContactNumber(patient.getEmergencyContactNumber());
        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());
        return response;
    }
}