package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Emergency;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findByPatient(Patient patient);
    List<Emergency> findByDoctor(Doctor doctor);
    List<Emergency> findByStatus(String status);
    List<Emergency> findBySeverity(String severity);
    List<Emergency> findByArrivalTimeBetween(LocalDateTime start, LocalDateTime end);
}