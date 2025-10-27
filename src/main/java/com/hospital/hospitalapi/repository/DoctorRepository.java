package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    Optional<Doctor> findByEmail(String email);
    
    Optional<Doctor> findByContactNumber(String contactNumber);
    
    List<Doctor> findByDepartmentId(Long departmentId);
    
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
    
    List<Doctor> findByIsAvailableTrue();
    
    List<Doctor> findByIsActiveTrue();
    
    List<Doctor> findByIsAvailableTrueAndIsActiveTrue();
}