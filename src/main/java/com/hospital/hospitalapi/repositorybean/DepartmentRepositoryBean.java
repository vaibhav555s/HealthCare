package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Department;
import com.hospital.hospitalapi.repository.DepartmentRepository;
import com.hospital.hospitalapi.exception.DepartmentNotFoundException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentRepositoryBean {
    
    private final DepartmentRepository departmentRepository;
    
    @Transactional
    public Department createDepartment(Department department) {
        log.info("Creating department: {}", department.getName());
        
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new DuplicateResourceException("Department already exists with name: " + department.getName());
        }
        
        Department saved = departmentRepository.save(department);
        log.info("Department created successfully with ID: {}", saved.getId());
        return saved;
    }
    
    public Department getDepartmentById(Long id) {
        log.debug("Fetching department by ID: {}", id);
        return departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + id));
    }
    
    public List<Department> getAllDepartments() {
        log.debug("Fetching all departments");
        return departmentRepository.findAll();
    }
    
    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        log.info("Updating department with ID: {}", id);
        Department department = getDepartmentById(id);
        
        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());
        department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
        department.setContactNumber(departmentDetails.getContactNumber());
        department.setLocation(departmentDetails.getLocation());
        department.setIsActive(departmentDetails.getIsActive());
        
        Department updated = departmentRepository.save(department);
        log.info("Department updated successfully: {}", id);
        return updated;
    }
    
    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
        log.info("Department deleted successfully: {}", id);
    }
}
