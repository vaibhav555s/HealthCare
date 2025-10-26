package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class WardRequest {
    private String wardNumber;
    private String wardType;
    private Integer totalBeds;
    private Double chargesPerDay;
    private String location;
}