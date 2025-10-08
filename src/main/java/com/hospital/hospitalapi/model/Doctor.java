package com.hospital.hospitalapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;
    private String qualification;
    private String email;
    private String phone;
    private int experience; // in years

    @ElementCollection
    private List<String> availability; // e.g. ["Mon 9-12", "Wed 2-5"]

    private boolean active = true;
}
