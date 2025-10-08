package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.MedicalHistory;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.repository.MedicalHistoryRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public MedicalHistory addMedicalHistory(Long patientId, MedicalHistory medicalHistory) {
        // Find the patient first to ensure they exist
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        
        // Associate the history with the patient and save
        medicalHistory.setPatient(patient);
        return medicalHistoryRepository.save(medicalHistory);
    }

    @Transactional(readOnly = true)
    public List<MedicalHistory> getMedicalHistoryForPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
        return medicalHistoryRepository.findByPatientId(patientId); // You'll need to create this method
    }
}