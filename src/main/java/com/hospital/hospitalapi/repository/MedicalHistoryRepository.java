package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hospitalapi.model.MedicalHistory;
import java.util.List;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    List<MedicalHistory> findByPatientId(Long patientId);
}
