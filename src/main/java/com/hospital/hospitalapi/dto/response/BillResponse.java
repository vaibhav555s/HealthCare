package com.hospital.hospitalapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillResponse {
    private Long id;
    private String patientName;
    private String billNumber;
    private LocalDateTime billDate;
    private Double totalAmount;
    private Double paidAmount;
    private Double balanceAmount;
    private String paymentStatus;
    private List<BillItemResponse> items;
}