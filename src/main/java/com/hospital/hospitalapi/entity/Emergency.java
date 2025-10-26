package com.hospital.hospitalapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emergency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @Column(nullable = false)
    private LocalDateTime arrivalTime;
    
    @Column(nullable = false, length = 20)
    private String severity; // CRITICAL, MODERATE, MINOR
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String chiefComplaint;
    
    @Column(columnDefinition = "TEXT")
    private String initialAssessment;
    
    @Column(nullable = false, length = 20)
    private String status; // WAITING, IN_TREATMENT, ADMITTED, DISCHARGED, TRANSFERRED
    
    @Column
    private LocalDateTime treatmentStartTime;
    
    @Column
    private LocalDateTime dischargeTime;
    
    @Column(columnDefinition = "TEXT")
    private String treatmentNotes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
