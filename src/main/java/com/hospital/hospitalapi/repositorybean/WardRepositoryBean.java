package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Ward;
import com.hospital.hospitalapi.repository.WardRepository;
import com.hospital.hospitalapi.exception.DepartmentNotFoundException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WardRepositoryBean {
    
    private final WardRepository wardRepository;
    
    @Transactional
    public Ward createWard(Ward ward) {
        log.info("Creating ward: {}", ward.getWardNumber());
        
        if (wardRepository.findByWardNumber(ward.getWardNumber()).isPresent()) {
            throw new DuplicateResourceException("Ward already exists with number: " + ward.getWardNumber());
        }
        
        ward.setAvailableBeds(ward.getTotalBeds());
        Ward saved = wardRepository.save(ward);
        log.info("Ward created with ID: {}", saved.getId());
        return saved;
    }
    
    public Ward getWardById(Long id) {
        log.debug("Fetching ward by ID: {}", id);
        return wardRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException("Ward not found with ID: " + id));
    }
    
    public List<Ward> getAllWards() {
        log.debug("Fetching all wards");
        return wardRepository.findAll();
    }
    
    public List<Ward> getWardsByType(String wardType) {
        log.debug("Fetching wards by type: {}", wardType);
        return wardRepository.findByWardType(wardType);
    }
    
    @Transactional
    public Ward updateWard(Long id, Ward wardDetails) {
        log.info("Updating ward with ID: {}", id);
        Ward ward = getWardById(id);
        
        ward.setWardNumber(wardDetails.getWardNumber());
        ward.setWardType(wardDetails.getWardType());
        ward.setTotalBeds(wardDetails.getTotalBeds());
        ward.setChargesPerDay(wardDetails.getChargesPerDay());
        ward.setLocation(wardDetails.getLocation());
        ward.setIsActive(wardDetails.getIsActive());
        
        return wardRepository.save(ward);
    }
    
    @Transactional
    public void updateAvailableBeds(Long wardId, int change) {
        Ward ward = getWardById(wardId);
        ward.setAvailableBeds(ward.getAvailableBeds() + change);
        wardRepository.save(ward);
    }
}
