package com.hospital.hospitalapi.dto.response;

import lombok.Data;

@Data
public class BillItemResponse {
    private Long id;
    private String itemType;
    private String description;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
