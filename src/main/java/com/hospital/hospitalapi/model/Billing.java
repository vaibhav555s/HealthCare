package com.hospital.hospitalapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "billing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String paymentStatus; // Pending, Paid, Failed
    private LocalDate billingDate;

    @ManyToOne
    private Patient patient;
}
