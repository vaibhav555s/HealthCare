package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hospitalapi.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
