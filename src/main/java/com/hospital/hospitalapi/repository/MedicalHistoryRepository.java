package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.MedicalHistory;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatient(Patient patient);
    List<MedicalHistory> findByDoctor(Doctor doctor);
    List<MedicalHistory> findByPatientOrderByVisitDateDesc(Patient patient);
    List<MedicalHistory> findByDiagnosisContainingIgnoreCase(String diagnosis);
}
