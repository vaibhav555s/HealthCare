package com.hospital.hospitalapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "referrals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Doctor referringDoctor;

    @ManyToOne
    private Doctor referredToDoctor;

    @ManyToOne
    private Patient patient;

    private String notes;
    private String status; // Sent, Accepted, Rejected
    private LocalDate dateCreated;
}
