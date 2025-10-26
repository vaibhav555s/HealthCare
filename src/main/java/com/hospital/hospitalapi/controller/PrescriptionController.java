package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Prescription;
import com.hospital.hospitalapi.entity.PrescriptionItem;
import com.hospital.hospitalapi.dto.request.PrescriptionRequest;
import com.hospital.hospitalapi.dto.request.PrescriptionItemRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.PrescriptionResponse;
import com.hospital.hospitalapi.dto.response.PrescriptionItemResponse;
import com.hospital.hospitalapi.repositorybean.PrescriptionRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription Management")
public class PrescriptionController {
    
    private final PrescriptionRepositoryBean prescriptionRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Create prescription")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(@RequestBody PrescriptionRequest request) {
        Prescription prescription = mapToEntity(request);
        Prescription created = prescriptionRepositoryBean.createPrescription(
            prescription, request.getPatientId(), request.getDoctorId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Prescription created", mapToResponse(created)));
    }
    
    @PostMapping("/{id}/items")
    @Operation(summary = "Add prescription item")
    public ResponseEntity<ApiResponse<PrescriptionItemResponse>> addPrescriptionItem(
            @PathVariable Long id, @RequestBody PrescriptionItemRequest request) {
        PrescriptionItem item = mapItemToEntity(request);
        PrescriptionItem created = prescriptionRepositoryBean.addPrescriptionItem(item, id, request.getMedicineId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Item added to prescription", mapItemToResponse(created)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionRepositoryBean.getPrescriptionById(id);
        return ResponseEntity.ok(ApiResponse.success("Prescription retrieved", mapToResponse(prescription)));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<PrescriptionResponse> prescriptions = prescriptionRepositoryBean.getPrescriptionsByPatient(patientId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Prescriptions retrieved", prescriptions));
    }
    
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deletePrescriptionItem(@PathVariable Long itemId) {
        prescriptionRepositoryBean.deletePrescriptionItem(itemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from prescription", null));
    }
    
    private Prescription mapToEntity(PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionDate(request.getPrescriptionDate());
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setInstructions(request.getInstructions());
        prescription.setFollowUpInstructions(request.getFollowUpInstructions());
        return prescription;
    }
    
    private PrescriptionItem mapItemToEntity(PrescriptionItemRequest request) {
        PrescriptionItem item = new PrescriptionItem();
        item.setDosage(request.getDosage());
        item.setFrequency(request.getFrequency());
        item.setDurationDays(request.getDurationDays());
        item.setQuantity(request.getQuantity());
        item.setInstructions(request.getInstructions());
        return item;
    }
    
    private PrescriptionResponse mapToResponse(Prescription prescription) {
        PrescriptionResponse response = new PrescriptionResponse();
        response.setId(prescription.getId());
        response.setPatientName(prescription.getPatient().getFirstName() + " " + prescription.getPatient().getLastName());
        response.setDoctorName(prescription.getDoctor().getFirstName() + " " + prescription.getDoctor().getLastName());
        response.setPrescriptionDate(prescription.getPrescriptionDate());
        response.setDiagnosis(prescription.getDiagnosis());
        response.setInstructions(prescription.getInstructions());
        response.setFollowUpInstructions(prescription.getFollowUpInstructions());
        
        if (prescription.getItems() != null) {
            List<PrescriptionItemResponse> items = prescription.getItems().stream()
                .map(this::mapItemToResponse).collect(Collectors.toList());
            response.setItems(items);
        }
        
        return response;
    }
    
    private PrescriptionItemResponse mapItemToResponse(PrescriptionItem item) {
        PrescriptionItemResponse response = new PrescriptionItemResponse();
        response.setId(item.getId());
        response.setMedicineName(item.getMedicine().getName());
        response.setDosage(item.getDosage());
        response.setFrequency(item.getFrequency());
        response.setDurationDays(item.getDurationDays());
        response.setQuantity(item.getQuantity());
        response.setInstructions(item.getInstructions());
        return response;
    }
}
