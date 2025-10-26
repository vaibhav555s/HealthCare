package com.hospital.hospitalapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "wards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String wardNumber;
    
    @Column(nullable = false, length = 50)
    private String wardType; // GENERAL, PRIVATE, ICU, EMERGENCY
    
    @Column(nullable = false)
    private Integer totalBeds;
    
    @Column(nullable = false)
    private Integer availableBeds;
    
    @Column(nullable = false)
    private Double chargesPerDay;
    
    @Column(length = 100)
    private String location;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
