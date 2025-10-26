package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.BedAllocationRepository;
import com.hospital.hospitalapi.exception.BedNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedAllocationRepositoryBean {
    
    private final BedAllocationRepository bedAllocationRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    private final BedRepositoryBean bedRepositoryBean;
    private final DoctorRepositoryBean doctorRepositoryBean;
    private final WardRepositoryBean wardRepositoryBean;
    
    @Transactional
    public BedAllocation allocateBed(BedAllocation allocation, Long patientId, Long bedId, Long doctorId) {
        log.info("Allocating bed ID: {} to patient ID: {}", bedId, patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        Bed bed = bedRepositoryBean.getBedById(bedId);
        Doctor doctor = doctorRepositoryBean.getDoctorById(doctorId);
        
        if (!"AVAILABLE".equals(bed.getStatus())) {
            throw new BedNotAvailableException("Bed is not available: " + bed.getBedNumber());
        }
        
        allocation.setPatient(patient);
        allocation.setBed(bed);
        allocation.setDoctor(doctor);
        allocation.setStatus("ADMITTED");
        
        bedRepositoryBean.updateBedStatus(bedId, "OCCUPIED");
        wardRepositoryBean.updateAvailableBeds(bed.getWard().getId(), -1);
        
        BedAllocation saved = bedAllocationRepository.save(allocation);
        log.info("Bed allocated successfully with ID: {}", saved.getId());
        return saved;
    }
    
    @Transactional
    public BedAllocation dischargePatient(Long allocationId, String dischargeNotes) {
        log.info("Discharging patient from allocation ID: {}", allocationId);
        
        BedAllocation allocation = bedAllocationRepository.findById(allocationId)
            .orElseThrow(() -> new BedNotAvailableException("Allocation not found"));
        
        allocation.setDischargeDate(LocalDateTime.now());
        allocation.setDischargeNotes(dischargeNotes);
        allocation.setStatus("DISCHARGED");
        
        bedRepositoryBean.updateBedStatus(allocation.getBed().getId(), "AVAILABLE");
        wardRepositoryBean.updateAvailableBeds(allocation.getBed().getWard().getId(), 1);
        
        return bedAllocationRepository.save(allocation);
    }
    
    public List<BedAllocation> getCurrentAdmissions() {
        return bedAllocationRepository.findByStatus("ADMITTED");
    }
}