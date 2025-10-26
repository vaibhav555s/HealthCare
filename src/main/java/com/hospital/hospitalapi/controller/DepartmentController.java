package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Department;
import com.hospital.hospitalapi.dto.request.DepartmentRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.DepartmentResponse;
import com.hospital.hospitalapi.repositorybean.DepartmentRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management")
public class DepartmentController {
    
    private final DepartmentRepositoryBean departmentRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Create department")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@RequestBody DepartmentRequest request) {
        Department department = mapToEntity(request);
        Department created = departmentRepositoryBean.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Department created successfully", mapToResponse(created)));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentRepositoryBean.getAllDepartments()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved", departments));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable Long id) {
        Department department = departmentRepositoryBean.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved", mapToResponse(department)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id, @RequestBody DepartmentRequest request) {
        Department department = mapToEntity(request);
        Department updated = departmentRepositoryBean.updateDepartment(id, department);
        return ResponseEntity.ok(ApiResponse.success("Department updated", mapToResponse(updated)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentRepositoryBean.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted", null));
    }
    
    private Department mapToEntity(DepartmentRequest request) {
        Department dept = new Department();
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());
        dept.setHeadOfDepartment(request.getHeadOfDepartment());
        dept.setContactNumber(request.getContactNumber());
        dept.setLocation(request.getLocation());
        return dept;
    }
    
    private DepartmentResponse mapToResponse(Department dept) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(dept.getId());
        response.setName(dept.getName());
        response.setDescription(dept.getDescription());
        response.setHeadOfDepartment(dept.getHeadOfDepartment());
        response.setContactNumber(dept.getContactNumber());
        response.setLocation(dept.getLocation());
        response.setIsActive(dept.getIsActive());
        response.setCreatedAt(dept.getCreatedAt());
        return response;
    }
}
