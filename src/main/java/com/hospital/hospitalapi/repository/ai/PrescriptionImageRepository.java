package com.hospital.hospitalapi.repository.ai;

import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionImageRepository extends JpaRepository<PrescriptionImage, Long> {
    List<PrescriptionImage> findByPrescription(Prescription prescription);
    List<PrescriptionImage> findByPatient(Patient patient);
    List<PrescriptionImage> findByAnalysisStatus(String status);
    List<PrescriptionImage> findByPatientOrderByUploadedAtDesc(Patient patient);
}