package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class BedRequest {
    private Long wardId;
    private String bedNumber;
    private String description;
}