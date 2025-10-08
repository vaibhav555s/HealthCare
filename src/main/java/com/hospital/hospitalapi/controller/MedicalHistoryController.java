package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.MedicalHistory;
import com.hospital.hospitalapi.repositorybean.MedicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/medical-history")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @PostMapping
    public ResponseEntity<MedicalHistory> addMedicalHistory(@PathVariable Long patientId, @RequestBody MedicalHistory medicalHistory) {
        try {
            MedicalHistory createdHistory = medicalHistoryService.addMedicalHistory(patientId, medicalHistory);
            return ResponseEntity.ok(createdHistory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistory>> getMedicalHistoryForPatient(@PathVariable Long patientId) {
        try {
            List<MedicalHistory> history = medicalHistoryService.getMedicalHistoryForPatient(patientId);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}