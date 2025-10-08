package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hospitalapi.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
