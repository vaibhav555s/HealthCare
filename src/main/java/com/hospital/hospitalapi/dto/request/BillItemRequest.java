package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class BillItemRequest {
    private Long billId;
    private String itemType;
    private String description;
    private Integer quantity;
    private Double unitPrice;
}
