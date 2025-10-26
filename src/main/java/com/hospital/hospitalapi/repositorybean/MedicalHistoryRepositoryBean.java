package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.MedicalHistoryRepository;
import com.hospital.hospitalapi.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalHistoryRepositoryBean {
    
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    private final DoctorRepositoryBean doctorRepositoryBean;
    
    @Transactional
    public MedicalHistory createMedicalHistory(MedicalHistory medicalHistory, Long patientId, Long doctorId) {
        log.info("Creating medical history for patient ID: {}", patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        
        medicalHistory.setPatient(patient);
        medicalHistory.setDoctor(doctor);
        
        MedicalHistory saved = medicalHistoryRepository.save(medicalHistory);
        log.info("Medical history created with ID: {}", saved.getId());
        return saved;
    }
    
    public MedicalHistory getMedicalHistoryById(Long id) {
        log.debug("Fetching medical history by ID: {}", id);
        return medicalHistoryRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Medical history not found with ID: " + id));
    }
    
    public List<MedicalHistory> getMedicalHistoryByPatient(Long patientId) {
        log.debug("Fetching medical history by patient ID: {}", patientId);
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        return medicalHistoryRepository.findByPatientOrderByVisitDateDesc(patient);
    }
    
    public List<MedicalHistory> searchByDiagnosis(String diagnosis) {
        log.debug("Searching medical history by diagnosis: {}", diagnosis);
        return medicalHistoryRepository.findByDiagnosisContainingIgnoreCase(diagnosis);
    }
}

