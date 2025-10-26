package com.hospital.hospitalapi.entity.ai;

import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Prescription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prescription_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(nullable = false, length = 500)
    private String imageUrl;
    
    @Column(nullable = false, length = 200)
    private String imageFileName;
    
    @Column(columnDefinition = "TEXT")
    private String extractedText;
    
    @Column(columnDefinition = "TEXT")
    private String aiAnalysisReport;
    
    @Column(nullable = false, length = 20)
    private String analysisStatus; // PENDING, ANALYZING, COMPLETED, FAILED
    
    @Column
    private Integer confidenceScore;
    
    @Column
    private LocalDateTime analyzedAt;
    
    @OneToMany(mappedBy = "prescriptionImage", cascade = CascadeType.ALL)
    private List<PrescriptionQuery> queries;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
