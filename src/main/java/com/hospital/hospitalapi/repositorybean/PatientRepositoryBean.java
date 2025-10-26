package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.exception.PatientNotFoundException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientRepositoryBean {
    
    private final PatientRepository patientRepository;
    
    @Transactional
    public Patient createPatient(Patient patient) {
        log.info("Creating patient: {} {}", patient.getFirstName(), patient.getLastName());
        
        if (patient.getContactNumber() != null && 
            patientRepository.findByContactNumber(patient.getContactNumber()).isPresent()) {
            throw new DuplicateResourceException("Contact number already exists");
        }
        
        if (patient.getEmail() != null && 
            patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        Patient saved = patientRepository.save(patient);
        log.info("Patient created successfully with ID: {}", saved.getId());
        return saved;
    }
    
    public Patient getPatientById(Long id) {
        log.debug("Fetching patient by ID: {}", id);
        return patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
    }
    
    public List<Patient> getAllPatients() {
        log.debug("Fetching all patients");
        return patientRepository.findAll();
    }
    
    @Transactional
    public Patient updatePatient(Long id, Patient patientDetails) {
        log.info("Updating patient with ID: {}", id);
        Patient patient = getPatientById(id);
        
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setContactNumber(patientDetails.getContactNumber());
        patient.setEmail(patientDetails.getEmail());
        patient.setAddress(patientDetails.getAddress());
        patient.setBloodGroup(patientDetails.getBloodGroup());
        patient.setAllergies(patientDetails.getAllergies());
        patient.setEmergencyContactName(patientDetails.getEmergencyContactName());
        patient.setEmergencyContactNumber(patientDetails.getEmergencyContactNumber());
        
        Patient updated = patientRepository.save(patient);
        log.info("Patient updated successfully: {}", id);
        return updated;
    }
    
    @Transactional
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
        log.info("Patient deleted successfully: {}", id);
    }
    
    public List<Patient> searchPatients(String query) {
        log.debug("Searching patients with query: {}", query);
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }
}
