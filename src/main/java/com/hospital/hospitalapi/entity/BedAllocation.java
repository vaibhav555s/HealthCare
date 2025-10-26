package com.hospital.hospitalapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bed_allocations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedAllocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(nullable = false)
    private LocalDateTime admissionDate;
    
    @Column
    private LocalDateTime dischargeDate;
    
    @Column(nullable = false, length = 20)
    private String status; // ADMITTED, DISCHARGED, TRANSFERRED
    
    @Column(columnDefinition = "TEXT")
    private String admissionReason;
    
    @Column(columnDefinition = "TEXT")
    private String dischargeNotes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
