package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyRepositoryBean {
    
    private final EmergencyRepository emergencyRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    private final DoctorRepositoryBean doctorRepositoryBean;
    
    @Transactional
    public Emergency createEmergency(Emergency emergency, Long patientId) {
        log.info("Creating emergency case for patient ID: {}", patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        emergency.setPatient(patient);
        emergency.setStatus("WAITING");
        
        Emergency saved = emergencyRepository.save(emergency);
        log.info("Emergency case created with ID: {}", saved.getId());
        return saved;
    }
    
    @Transactional
    public Emergency assignDoctor(Long emergencyId, Long doctorId) {
        log.info("Assigning doctor ID: {} to emergency ID: {}", doctorId, emergencyId);
        
        Emergency emergency = getEmergencyById(emergencyId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        
        emergency.setDoctor(doctor);
        emergency.setStatus("IN_TREATMENT");
        emergency.setTreatmentStartTime(LocalDateTime.now());
        
        return emergencyRepository.save(emergency);
    }
    
    @Transactional
    public Emergency updateStatus(Long id, String status) {
        Emergency emergency = getEmergencyById(id);
        emergency.setStatus(status);
        
        if ("DISCHARGED".equals(status) || "TRANSFERRED".equals(status)) {
            emergency.setDischargeTime(LocalDateTime.now());
        }
        
        return emergencyRepository.save(emergency);
    }
    
    public Emergency getEmergencyById(Long id) {
        return emergencyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Emergency not found with ID: " + id));
    }
    
    public List<Emergency> getTodayEmergencies() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return emergencyRepository.findByArrivalTimeBetween(startOfDay, endOfDay);
    }
}
