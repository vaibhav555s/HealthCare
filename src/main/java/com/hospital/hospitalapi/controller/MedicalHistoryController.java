package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.MedicalHistory;
import com.hospital.hospitalapi.dto.request.MedicalHistoryRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.MedicalHistoryResponse;
import com.hospital.hospitalapi.repositorybean.MedicalHistoryRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-history")
@RequiredArgsConstructor
@Tag(name = "Medical History Management")
public class MedicalHistoryController {
    
    private final MedicalHistoryRepositoryBean medicalHistoryRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Record consultation")
    public ResponseEntity<ApiResponse<MedicalHistoryResponse>> createMedicalHistory(
            @RequestBody MedicalHistoryRequest request) {
        MedicalHistory medicalHistory = mapToEntity(request);
        MedicalHistory created = medicalHistoryRepositoryBean.createMedicalHistory(
            medicalHistory, request.getPatientId(), request.getDoctorId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Medical history recorded", mapToResponse(created)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalHistoryResponse>> getMedicalHistoryById(@PathVariable Long id) {
        MedicalHistory medicalHistory = medicalHistoryRepositoryBean.getMedicalHistoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Medical history retrieved", mapToResponse(medicalHistory)));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<MedicalHistoryResponse>>> getPatientMedicalHistory(
            @PathVariable Long patientId) {
        List<MedicalHistoryResponse> history = medicalHistoryRepositoryBean.getMedicalHistoryByPatient(patientId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Patient history retrieved", history));
    }
    
    @GetMapping("/patient/{patientId}/recent")
    public ResponseEntity<ApiResponse<List<MedicalHistoryResponse>>> getRecentConsultations(
            @PathVariable Long patientId) {
        List<MedicalHistoryResponse> history = medicalHistoryRepositoryBean.getMedicalHistoryByPatient(patientId)
            .stream().limit(5).map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Recent consultations", history));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicalHistoryResponse>>> searchByDiagnosis(
            @RequestParam String diagnosis) {
        List<MedicalHistoryResponse> history = medicalHistoryRepositoryBean.searchByDiagnosis(diagnosis)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search results", history));
    }
    
    private MedicalHistory mapToEntity(MedicalHistoryRequest request) {
        MedicalHistory history = new MedicalHistory();
        history.setVisitDate(request.getVisitDate());
        history.setSymptoms(request.getSymptoms());
        history.setDiagnosis(request.getDiagnosis());
        history.setTreatment(request.getTreatment());
        history.setBloodPressure(request.getBloodPressure());
        history.setTemperature(request.getTemperature());
        history.setPulse(request.getPulse());
        history.setWeight(request.getWeight());
        history.setHeight(request.getHeight());
        history.setNotes(request.getNotes());
        return history;
    }
    
    private MedicalHistoryResponse mapToResponse(MedicalHistory history) {
        MedicalHistoryResponse response = new MedicalHistoryResponse();
        response.setId(history.getId());
        response.setPatientName(history.getPatient().getFirstName() + " " + history.getPatient().getLastName());
        response.setDoctorName(history.getDoctor().getFirstName() + " " + history.getDoctor().getLastName());
        response.setVisitDate(history.getVisitDate());
        response.setSymptoms(history.getSymptoms());
        response.setDiagnosis(history.getDiagnosis());
        response.setTreatment(history.getTreatment());
        response.setBloodPressure(history.getBloodPressure());
        response.setTemperature(history.getTemperature());
        response.setPulse(history.getPulse());
        response.setWeight(history.getWeight());
        response.setHeight(history.getHeight());
        response.setNotes(history.getNotes());
        return response;
    }
}
