package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.dto.request.DoctorRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.DoctorResponse;
import com.hospital.hospitalapi.repositorybean.DoctorRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management")
public class DoctorController {
    
    private final DoctorRepositoryBean doctorRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Register new doctor")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody DoctorRequest request) {
        Doctor doctor = mapToEntity(request);
        Doctor created = doctorRepositoryBean.createDoctor(doctor, request.getDepartmentId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Doctor created successfully", mapToResponse(created)));
    }
    
    @GetMapping
    @Operation(summary = "Get all doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorRepositoryBean.getAllDoctors()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved", doctors));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepositoryBean.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor retrieved", mapToResponse(doctor)));
    }
    
    @GetMapping("/department/{deptId}")
    @Operation(summary = "Get doctors by department")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsByDepartment(@PathVariable Long deptId) {
        List<DoctorResponse> doctors = doctorRepositoryBean.getDoctorsByDepartment(deptId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved", doctors));
    }
    
    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get doctors by specialization")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        List<DoctorResponse> doctors = doctorRepositoryBean.getDoctorsBySpecialization(specialization)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved", doctors));
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getActiveDoctors() {
        List<DoctorResponse> doctors = doctorRepositoryBean.getActiveDoctors()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Active doctors retrieved", doctors));
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get all available doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAvailableDoctors() {
        List<DoctorResponse> doctors = doctorRepositoryBean.getAvailableDoctors()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Available doctors retrieved", doctors));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable Long id, @RequestBody DoctorRequest request) {
        Doctor doctor = mapToEntity(request);
        Doctor updated = doctorRepositoryBean.updateDoctor(id, doctor, request.getDepartmentId());
        return ResponseEntity.ok(ApiResponse.success("Doctor updated", mapToResponse(updated)));
    }
    
    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update doctor availability status")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateAvailability(
            @PathVariable Long id, @RequestParam Boolean isAvailable) {
        Doctor updated = doctorRepositoryBean.updateAvailability(id, isAvailable);
        return ResponseEntity.ok(ApiResponse.success("Doctor availability updated", mapToResponse(updated)));
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate doctor")
    public ResponseEntity<ApiResponse<DoctorResponse>> activateDoctor(@PathVariable Long id) {
        Doctor updated = doctorRepositoryBean.updateActiveStatus(id, true);
        return ResponseEntity.ok(ApiResponse.success("Doctor activated", mapToResponse(updated)));
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate doctor (soft delete)")
    public ResponseEntity<ApiResponse<DoctorResponse>> deactivateDoctor(@PathVariable Long id) {
        Doctor updated = doctorRepositoryBean.updateActiveStatus(id, false);
        return ResponseEntity.ok(ApiResponse.success("Doctor deactivated", mapToResponse(updated)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor permanently")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorRepositoryBean.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted", null));
    }
    
    private Doctor mapToEntity(DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setContactNumber(request.getContactNumber());
        doctor.setEmail(request.getEmail());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());
        
        if (request.getIsAvailable() != null) {
            doctor.setIsAvailable(request.getIsAvailable());
        }
        if (request.getIsActive() != null) {
            doctor.setIsActive(request.getIsActive());
        }
        
        return doctor;
    }
    
    private DoctorResponse mapToResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setLicenseNumber(doctor.getLicenseNumber());
        response.setSpecialization(doctor.getSpecialization());
        response.setQualification(doctor.getQualification());
        response.setContactNumber(doctor.getContactNumber());
        response.setEmail(doctor.getEmail());
        response.setDepartmentName(doctor.getDepartment().getName());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setIsAvailable(doctor.getIsAvailable());
        response.setIsActive(doctor.getIsActive());
        response.setCreatedAt(doctor.getCreatedAt());
        response.setUpdatedAt(doctor.getUpdatedAt());
        return response;
    }
}