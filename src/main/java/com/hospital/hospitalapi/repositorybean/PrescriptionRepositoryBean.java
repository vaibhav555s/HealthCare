package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.PrescriptionRepository;
import com.hospital.hospitalapi.repository.PrescriptionItemRepository;
import com.hospital.hospitalapi.exception.PrescriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionRepositoryBean {
    
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    private final DoctorRepositoryBean doctorRepositoryBean;
    private final MedicineRepositoryBean medicineRepositoryBean;
    
    @Transactional
    public Prescription createPrescription(Prescription prescription, Long patientId, Long doctorId) {
        log.info("Creating prescription for patient ID: {}", patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        
        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription created with ID: {}", saved.getId());
        return saved;
    }
    
    @Transactional
    public PrescriptionItem addPrescriptionItem(PrescriptionItem item, Long prescriptionId, Long medicineId) {
        log.info("Adding item to prescription ID: {}", prescriptionId);
        
        Prescription prescription = getPrescriptionById(prescriptionId);
        Medicine medicine = medicineRepositoryBean.getMedicineById(medicineId);
        
        item.setPrescription(prescription);
        item.setMedicine(medicine);
        
        PrescriptionItem saved = prescriptionItemRepository.save(item);
        log.info("Prescription item added with ID: {}", saved.getId());
        return saved;
    }
    
    public Prescription getPrescriptionById(Long id) {
        log.debug("Fetching prescription by ID: {}", id);
        return prescriptionRepository.findById(id)
            .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with ID: " + id));
    }
    
    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        log.debug("Fetching prescriptions by patient ID: {}", patientId);
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        return prescriptionRepository.findByPatientOrderByPrescriptionDateDesc(patient);
    }
    
    @Transactional
    public void deletePrescriptionItem(Long itemId) {
        log.info("Deleting prescription item: {}", itemId);
        prescriptionItemRepository.deleteById(itemId);
    }
}
