package com.hospital.hospitalapi.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BedAllocationRequest {
    private Long patientId;
    private Long bedId;
    private Long doctorId;
    private LocalDateTime admissionDate;
    private String admissionReason;
}