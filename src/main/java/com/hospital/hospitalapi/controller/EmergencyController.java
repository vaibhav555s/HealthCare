package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Emergency;
import com.hospital.hospitalapi.dto.request.EmergencyRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.EmergencyResponse;
import com.hospital.hospitalapi.repositorybean.EmergencyRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emergencies")
@RequiredArgsConstructor
@Tag(name = "Emergency Management")
public class EmergencyController {
    
    private final EmergencyRepositoryBean emergencyRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Register emergency case")
    public ResponseEntity<ApiResponse<EmergencyResponse>> createEmergency(@RequestBody EmergencyRequest request) {
        Emergency emergency = mapToEntity(request);
        Emergency created = emergencyRepositoryBean.createEmergency(emergency, request.getPatientId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Emergency registered", mapToResponse(created)));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmergencyResponse>>> getAllEmergencies() {
        List<EmergencyResponse> emergencies = emergencyRepositoryBean.getTodayEmergencies()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Emergencies retrieved", emergencies));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmergencyResponse>> getEmergencyById(@PathVariable Long id) {
        Emergency emergency = emergencyRepositoryBean.getEmergencyById(id);
        return ResponseEntity.ok(ApiResponse.success("Emergency retrieved", mapToResponse(emergency)));
    }
    
    @PutMapping("/{id}/assign-doctor")
    @Operation(summary = "Assign doctor to emergency")
    public ResponseEntity<ApiResponse<EmergencyResponse>> assignDoctor(
            @PathVariable Long id, @RequestParam Long doctorId) {
        Emergency updated = emergencyRepositoryBean.assignDoctor(id, doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor assigned", mapToResponse(updated)));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update emergency status")
    public ResponseEntity<ApiResponse<EmergencyResponse>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        Emergency updated = emergencyRepositoryBean.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated", mapToResponse(updated)));
    }
    
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<EmergencyResponse>>> getTodayEmergencies() {
        List<EmergencyResponse> emergencies = emergencyRepositoryBean.getTodayEmergencies()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Today's emergencies", emergencies));
    }
    
    private Emergency mapToEntity(EmergencyRequest request) {
        Emergency emergency = new Emergency();
        emergency.setArrivalTime(request.getArrivalTime());
        emergency.setSeverity(request.getSeverity());
        emergency.setChiefComplaint(request.getChiefComplaint());
        emergency.setInitialAssessment(request.getInitialAssessment());
        return emergency;
    }
    
    private EmergencyResponse mapToResponse(Emergency emergency) {
        EmergencyResponse response = new EmergencyResponse();
        response.setId(emergency.getId());
        response.setPatientName(emergency.getPatient().getFirstName() + " " + emergency.getPatient().getLastName());
        if (emergency.getDoctor() != null) {
            response.setDoctorName(emergency.getDoctor().getFirstName() + " " + emergency.getDoctor().getLastName());
        }
        response.setArrivalTime(emergency.getArrivalTime());
        response.setSeverity(emergency.getSeverity());
        response.setChiefComplaint(emergency.getChiefComplaint());
        response.setStatus(emergency.getStatus());
        response.setTreatmentStartTime(emergency.getTreatmentStartTime());
        return response;
    }
}
