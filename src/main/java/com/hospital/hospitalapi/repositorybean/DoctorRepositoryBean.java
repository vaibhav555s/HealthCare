package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Department;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.DepartmentRepository;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorRepositoryBean {
    
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    
    @Transactional
    public Doctor createDoctor(Doctor doctor, Long departmentId) {
        log.info("Creating doctor: {} {}", doctor.getFirstName(), doctor.getLastName());
        
        if (doctor.getLicenseNumber() != null && 
            doctorRepository.findByLicenseNumber(doctor.getLicenseNumber()).isPresent()) {
            throw new DuplicateResourceException("License number already exists");
        }
        
        if (doctor.getEmail() != null && 
            doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        if (doctor.getContactNumber() != null && 
            doctorRepository.findByContactNumber(doctor.getContactNumber()).isPresent()) {
            throw new DuplicateResourceException("Contact number already exists");
        }
        
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
        doctor.setDepartment(department);
        
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor created successfully with ID: {}", saved.getId());
        return saved;
    }
    
    public Doctor getDoctorById(Long id) {
        log.debug("Fetching doctor by ID: {}", id);
        return doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
    }
    
    public List<Doctor> getAllDoctors() {
        log.debug("Fetching all doctors");
        return doctorRepository.findAll();
    }
    
    public List<Doctor> getActiveDoctors() {
        log.debug("Fetching all active doctors");
        return doctorRepository.findByIsActiveTrue();
    }
    
    public List<Doctor> getAvailableDoctors() {
        log.debug("Fetching all available doctors");
        return doctorRepository.findByIsAvailableTrueAndIsActiveTrue();
    }
    
    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        log.debug("Fetching doctors by department ID: {}", departmentId);
        return doctorRepository.findByDepartmentId(departmentId);
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        log.debug("Fetching doctors by specialization: {}", specialization);
        return doctorRepository.findBySpecializationIgnoreCase(specialization);
    }
    
    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctorDetails, Long departmentId) {
        log.info("Updating doctor with ID: {}", id);
        Doctor doctor = getDoctorById(id);
        
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setLicenseNumber(doctorDetails.getLicenseNumber());
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setQualification(doctorDetails.getQualification());
        doctor.setContactNumber(doctorDetails.getContactNumber());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setExperienceYears(doctorDetails.getExperienceYears());
        doctor.setConsultationFee(doctorDetails.getConsultationFee());
        
        if (doctorDetails.getIsAvailable() != null) {
            doctor.setIsAvailable(doctorDetails.getIsAvailable());
        }
        
        if (doctorDetails.getIsActive() != null) {
            doctor.setIsActive(doctorDetails.getIsActive());
        }
        
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
            doctor.setDepartment(department);
        }
        
        Doctor updated = doctorRepository.save(doctor);
        log.info("Doctor updated successfully: {}", id);
        return updated;
    }
    
    @Transactional
    public Doctor updateAvailability(Long id, Boolean isAvailable) {
        log.info("Updating availability for doctor ID: {} to {}", id, isAvailable);
        Doctor doctor = getDoctorById(id);
        doctor.setIsAvailable(isAvailable);
        Doctor updated = doctorRepository.save(doctor);
        log.info("Doctor availability updated successfully: {}", id);
        return updated;
    }
    
    @Transactional
    public Doctor updateActiveStatus(Long id, Boolean isActive) {
        log.info("Updating active status for doctor ID: {} to {}", id, isActive);
        Doctor doctor = getDoctorById(id);
        doctor.setIsActive(isActive);
        Doctor updated = doctorRepository.save(doctor);
        log.info("Doctor active status updated successfully: {}", id);
        return updated;
    }
    
    @Transactional
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
        log.info("Doctor deleted successfully: {}", id);
    }
}