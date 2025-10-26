package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class DepartmentRequest {
    private String name;
    private String description;
    private String headOfDepartment;
    private String contactNumber;
    private String location;
}
