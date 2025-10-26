package com.hospital.hospitalapi.repository.ai;

import com.hospital.hospitalapi.entity.ai.PrescriptionQuery;
import com.hospital.hospitalapi.entity.ai.PrescriptionImage;
import com.hospital.hospitalapi.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionQueryRepository extends JpaRepository<PrescriptionQuery, Long> {
    List<PrescriptionQuery> findByPrescriptionImage(PrescriptionImage prescriptionImage);
    List<PrescriptionQuery> findByPatient(Patient patient);
    List<PrescriptionQuery> findByPrescriptionImageOrderByAskedAtAsc(PrescriptionImage prescriptionImage);
}