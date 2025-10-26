package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Bed;
import com.hospital.hospitalapi.entity.Ward;
import com.hospital.hospitalapi.repository.BedRepository;
import com.hospital.hospitalapi.exception.BedNotAvailableException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedRepositoryBean {
    
    private final BedRepository bedRepository;
    private final WardRepositoryBean wardRepositoryBean;
    
    @Transactional
    public Bed createBed(Bed bed, Long wardId) {
        log.info("Creating bed: {}", bed.getBedNumber());
        
        if (bedRepository.findByBedNumber(bed.getBedNumber()).isPresent()) {
            throw new DuplicateResourceException("Bed already exists with number: " + bed.getBedNumber());
        }
        
        Ward ward = wardRepositoryBean.getWardById(wardId);
        bed.setWard(ward);
        bed.setStatus("AVAILABLE");
        
        Bed saved = bedRepository.save(bed);
        log.info("Bed created with ID: {}", saved.getId());
        return saved;
    }
    
    public Bed getBedById(Long id) {
        log.debug("Fetching bed by ID: {}", id);
        return bedRepository.findById(id)
            .orElseThrow(() -> new BedNotAvailableException("Bed not found with ID: " + id));
    }
    
    public List<Bed> getAvailableBeds(String wardType) {
        log.debug("Fetching available beds for ward type: {}", wardType);
        return bedRepository.findByStatus("AVAILABLE");
    }
    
    @Transactional
    public Bed updateBedStatus(Long id, String status) {
        log.info("Updating bed status: {} to {}", id, status);
        Bed bed = getBedById(id);
        bed.setStatus(status);
        return bedRepository.save(bed);
    }
}
