package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private String headOfDepartment;
    private String contactNumber;
    private String location;
    private Boolean isActive;
    private LocalDateTime createdAt;
}