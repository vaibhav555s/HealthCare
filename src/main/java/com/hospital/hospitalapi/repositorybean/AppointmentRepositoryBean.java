package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.AppointmentRepository;
import com.hospital.hospitalapi.exception.AppointmentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentRepositoryBean {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    private final DoctorRepositoryBean doctorRepositoryBean;
    private final DepartmentRepositoryBean departmentRepositoryBean;
    
    @Transactional
    public Appointment createAppointment(Appointment appointment, Long patientId, Long doctorId, Long departmentId) {
        log.info("Creating appointment for patient ID: {}", patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        Department department = departmentRepositoryBean.getDepartmentById(departmentId);
        
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setTokenNumber(generateTokenNumber());
        appointment.setStatus("SCHEDULED");
        
        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment created with token: {}", saved.getTokenNumber());
        return saved;
    }
    
    private String generateTokenNumber() {
        return "TKN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    public Appointment getAppointmentById(Long id) {
        log.debug("Fetching appointment by ID: {}", id);
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + id));
    }
    
    public List<Appointment> getAllAppointments() {
        log.debug("Fetching all appointments");
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        log.debug("Fetching appointments by patient ID: {}", patientId);
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        return appointmentRepository.findByPatient(patient);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        log.debug("Fetching appointments by doctor ID: {}", doctorId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        return appointmentRepository.findByDoctor(doctor);
    }
    
    public List<Appointment> getTodayAppointments() {
        log.debug("Fetching today's appointments");
        return appointmentRepository.findByAppointmentDate(LocalDate.now());
    }
    
    @Transactional
    public Appointment updateAppointmentStatus(Long id, String status) {
        log.info("Updating appointment status: {} to {}", id, status);
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public void cancelAppointment(Long id) {
        log.info("Cancelling appointment: {}", id);
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }
}
