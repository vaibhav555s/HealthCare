package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Double amount;
    private String paymentMethod;
    private String transactionId;
}