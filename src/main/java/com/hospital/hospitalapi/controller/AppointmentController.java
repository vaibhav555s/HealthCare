package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Appointment;
import com.hospital.hospitalapi.dto.request.AppointmentRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.AppointmentResponse;
import com.hospital.hospitalapi.repositorybean.AppointmentRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management")
public class AppointmentController {
    
    private final AppointmentRepositoryBean appointmentRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Book appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@RequestBody AppointmentRequest request) {
        Appointment appointment = mapToEntity(request);
        Appointment created = appointmentRepositoryBean.createAppointment(
            appointment, request.getPatientId(), request.getDoctorId(), request.getDepartmentId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Appointment booked successfully", mapToResponse(created)));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentRepositoryBean.getAllAppointments()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentRepositoryBean.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved", mapToResponse(appointment)));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentRepositoryBean.getAppointmentsByPatient(patientId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<AppointmentResponse> appointments = appointmentRepositoryBean.getAppointmentsByDoctor(doctorId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }
    
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getTodayAppointments() {
        List<AppointmentResponse> appointments = appointmentRepositoryBean.getTodayAppointments()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Today's appointments", appointments));
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(@PathVariable Long id) {
        Appointment updated = appointmentRepositoryBean.updateAppointmentStatus(id, "COMPLETED");
        return ResponseEntity.ok(ApiResponse.success("Appointment completed", mapToResponse(updated)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Long id) {
        appointmentRepositoryBean.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled", null));
    }
    
    private Appointment mapToEntity(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        return appointment;
    }
    
    private AppointmentResponse mapToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        response.setDoctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
        response.setDepartmentName(appointment.getDepartment().getName());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setTokenNumber(appointment.getTokenNumber());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        return response;
    }
}
