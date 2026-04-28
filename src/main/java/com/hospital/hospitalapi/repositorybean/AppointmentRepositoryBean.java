package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.dto.request.ConsultationRequest;
import com.hospital.hospitalapi.dto.request.FeedbackRequest;
import com.hospital.hospitalapi.repository.AppointmentRepository;
import com.hospital.hospitalapi.repository.MedicalHistoryRepository;
import com.hospital.hospitalapi.repository.PrescriptionRepository;
import com.hospital.hospitalapi.exception.AppointmentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PrescriptionRepository prescriptionRepository;
    
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
    
    /**
     * Complete appointment with full consultation data.
     * This is the core clinical workflow:
     * 1. Updates appointment status to COMPLETED
     * 2. Stores diagnosis + medicines on the appointment itself
     * 3. Creates a MedicalHistory record (for patient timeline)
     * 4. Creates a Prescription record (for patient records)
     * 5. Optionally creates a follow-up appointment
     */
    @Transactional
    public Appointment completeWithConsultation(Long appointmentId, ConsultationRequest request) {
        log.info("Completing appointment {} with consultation data", appointmentId);
        
        Appointment appointment = getAppointmentById(appointmentId);
        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        
        // 1. Update appointment with consultation summary
        appointment.setStatus("COMPLETED");
        appointment.setDiagnosis(request.getDiagnosis());
        appointment.setMedicines(request.getMedicines());
        appointment.setConsultationNotes(request.getNotes());
        
        // 2. Create Medical History record
        MedicalHistory history = new MedicalHistory();
        history.setPatient(patient);
        history.setDoctor(doctor);
        history.setAppointment(appointment);
        history.setVisitDate(LocalDateTime.now());
        history.setSymptoms(request.getSymptoms() != null ? request.getSymptoms() : appointment.getReason());
        history.setDiagnosis(request.getDiagnosis());
        history.setTreatment(request.getTreatment() != null ? request.getTreatment() : request.getMedicines());
        history.setBloodPressure(request.getBloodPressure());
        history.setTemperature(request.getTemperature());
        history.setPulse(request.getPulse());
        history.setWeight(request.getWeight());
        history.setHeight(request.getHeight());
        history.setNotes(request.getNotes());
        
        MedicalHistory savedHistory = medicalHistoryRepository.save(history);
        log.info("Medical history created with ID: {}", savedHistory.getId());
        
        // 3. Create Prescription record
        if (request.getMedicines() != null && !request.getMedicines().isBlank()) {
            Prescription prescription = new Prescription();
            prescription.setPatient(patient);
            prescription.setDoctor(doctor);
            prescription.setMedicalHistory(savedHistory);
            prescription.setPrescriptionDate(LocalDateTime.now());
            prescription.setDiagnosis(request.getDiagnosis());
            prescription.setInstructions(request.getInstructions());
            prescription.setFollowUpInstructions(request.getFollowUpInstructions());
            
            prescriptionRepository.save(prescription);
            log.info("Prescription created for appointment {}", appointmentId);
        }
        
        // 4. Create follow-up appointment if requested
        if (request.getFollowUpDate() != null && !request.getFollowUpDate().isBlank()) {
            try {
                LocalDate followUpDate = LocalDate.parse(request.getFollowUpDate());
                Appointment followUp = new Appointment();
                followUp.setPatient(patient);
                followUp.setDoctor(doctor);
                followUp.setDepartment(appointment.getDepartment());
                followUp.setAppointmentDate(followUpDate);
                followUp.setAppointmentTime(appointment.getAppointmentTime());
                followUp.setTokenNumber(generateTokenNumber());
                followUp.setStatus("SCHEDULED");
                followUp.setReason("Follow-up: " + request.getDiagnosis());
                followUp.setNotes("Follow-up appointment from " + appointment.getAppointmentDate());
                
                appointmentRepository.save(followUp);
                log.info("Follow-up appointment created for {}", followUpDate);
            } catch (Exception e) {
                log.warn("Failed to create follow-up appointment: {}", e.getMessage());
            }
        }
        
        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment {} completed with full consultation", appointmentId);
        return saved;
    }
    
    /**
     * Submit patient feedback for a completed appointment.
     */
    @Transactional
    public Appointment submitFeedback(Long appointmentId, FeedbackRequest request) {
        log.info("Submitting feedback for appointment {}", appointmentId);
        Appointment appointment = getAppointmentById(appointmentId);
        
        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new RuntimeException("Can only submit feedback for completed appointments");
        }
        
        if (request.getRating() != null && (request.getRating() < 1 || request.getRating() > 5)) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        appointment.setRating(request.getRating());
        appointment.setFeedbackComment(request.getComment());
        return appointmentRepository.save(appointment);
    }
    
    private String generateTokenNumber() {
        return "TKN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
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
