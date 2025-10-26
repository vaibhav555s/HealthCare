package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Department;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.exception.DoctorNotFoundException;
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
    private final DepartmentRepositoryBean departmentRepositoryBean;
    
    @Transactional
    public Doctor createDoctor(Doctor doctor, Long departmentId) {
        log.info("Creating doctor: {} {}", doctor.getFirstName(), doctor.getLastName());
        
        if (doctorRepository.findByLicenseNumber(doctor.getLicenseNumber()).isPresent()) {
            throw new DuplicateResourceException("License number already exists");
        }
        
        Department department = departmentRepositoryBean.getDepartmentById(departmentId);
        doctor.setDepartment(department);
        
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor created successfully with ID: {}", saved.getId());
        return saved;
    }
    
    public Doctor getDoctorById(Long id) {
        log.debug("Fetching doctor by ID: {}", id);
        return doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + id));
    }
    
    public List<Doctor> getAllDoctors() {
        log.debug("Fetching all doctors");
        return doctorRepository.findAll();
    }
    
    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        log.debug("Fetching doctors by department ID: {}", departmentId);
        Department department = departmentRepositoryBean.getDepartmentById(departmentId);
        return doctorRepository.findByDepartment(department);
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        log.debug("Fetching doctors by specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization);
    }
    
    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctorDetails, Long departmentId) {
        log.info("Updating doctor with ID: {}", id);
        Doctor doctor = getDoctorById(id);
        
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setQualification(doctorDetails.getQualification());
        doctor.setContactNumber(doctorDetails.getContactNumber());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setExperienceYears(doctorDetails.getExperienceYears());
        doctor.setConsultationFee(doctorDetails.getConsultationFee());
        doctor.setIsAvailable(doctorDetails.getIsAvailable());
        
        if (departmentId != null) {
            Department department = departmentRepositoryBean.getDepartmentById(departmentId);
            doctor.setDepartment(department);
        }
        
        Doctor updated = doctorRepository.save(doctor);
        log.info("Doctor updated successfully: {}", id);
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

