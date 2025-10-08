package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hospitalapi.entity.Prescription;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientId(Long patientId);
}
