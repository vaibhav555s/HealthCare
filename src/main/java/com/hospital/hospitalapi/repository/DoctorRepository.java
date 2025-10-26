package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    List<Doctor> findByDepartment(Department department);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByIsAvailable(Boolean isAvailable);
}
