package com.hospital.hospitalapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role; // "ADMIN", "DOCTOR", "PATIENT"
    private String phone;
    private String gender;

    @Column(nullable = true)
    private String address;
}
