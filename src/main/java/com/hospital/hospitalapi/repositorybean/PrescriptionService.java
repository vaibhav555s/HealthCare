package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Prescription;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public Prescription createPrescription(Prescription prescription) {
        // Business Logic: Verify patient and doctor exist
        Patient patient = patientRepository.findById(prescription.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(prescription.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        
        return prescriptionRepository.save(prescription);
    }

    @Transactional(readOnly = true)
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsForPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found");
        }
        return prescriptionRepository.findByPatientId(patientId); // You'll need to create this method
    }

    @Transactional
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found");
        }
        prescriptionRepository.deleteById(id);
    }
}