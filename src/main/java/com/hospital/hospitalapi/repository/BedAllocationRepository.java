package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.BedAllocation;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BedAllocationRepository extends JpaRepository<BedAllocation, Long> {
    List<BedAllocation> findByPatient(Patient patient);
    Optional<BedAllocation> findByPatientAndStatus(Patient patient, String status);
    List<BedAllocation> findByBed(Bed bed);
    List<BedAllocation> findByStatus(String status);
}
