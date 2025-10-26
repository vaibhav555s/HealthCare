package com.hospital.hospitalapi.entity.ai;

import com.hospital.hospitalapi.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionQuery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prescription_image_id", nullable = false)
    private PrescriptionImage prescriptionImage;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String userQuestion;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String aiResponse;
    
    @Column(nullable = false)
    private LocalDateTime askedAt = LocalDateTime.now();
}

