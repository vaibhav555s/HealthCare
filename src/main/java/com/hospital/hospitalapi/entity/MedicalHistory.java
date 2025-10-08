package com.hospital.hospitalapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conditionName;
    private String treatment;
    private LocalDate dateRecorded;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}

