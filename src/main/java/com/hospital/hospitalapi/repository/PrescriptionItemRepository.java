package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.PrescriptionItem;
import com.hospital.hospitalapi.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {
    List<PrescriptionItem> findByPrescription(Prescription prescription);
}
