package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Prescription;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(Patient patient);
    List<Prescription> findByDoctor(Doctor doctor);
    List<Prescription> findByPatientOrderByPrescriptionDateDesc(Patient patient);
}
