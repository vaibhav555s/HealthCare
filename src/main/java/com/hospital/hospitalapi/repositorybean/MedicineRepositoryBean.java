package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Medicine;
import com.hospital.hospitalapi.repository.MedicineRepository;
import com.hospital.hospitalapi.exception.MedicineNotFoundException;
import com.hospital.hospitalapi.exception.InsufficientStockException;
import com.hospital.hospitalapi.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineRepositoryBean {
    
    private final MedicineRepository medicineRepository;
    
    @Transactional
    public Medicine createMedicine(Medicine medicine) {
        log.info("Creating medicine: {}", medicine.getName());
        
        if (medicineRepository.findByName(medicine.getName()).isPresent()) {
            throw new DuplicateResourceException("Medicine already exists with name: " + medicine.getName());
        }
        
        Medicine saved = medicineRepository.save(medicine);
        log.info("Medicine created with ID: {}", saved.getId());
        return saved;
    }
    
    public Medicine getMedicineById(Long id) {
        log.debug("Fetching medicine by ID: {}", id);
        return medicineRepository.findById(id)
            .orElseThrow(() -> new MedicineNotFoundException("Medicine not found with ID: " + id));
    }
    
    public List<Medicine> getAllMedicines() {
        log.debug("Fetching all medicines");
        return medicineRepository.findAll();
    }
    
    public List<Medicine> searchMedicines(String name) {
        log.debug("Searching medicines with name: {}", name);
        return medicineRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Medicine> getLowStockMedicines(Integer threshold) {
        log.debug("Fetching low stock medicines with threshold: {}", threshold);
        return medicineRepository.findByStockQuantityLessThanEqual(threshold);
    }
    
    @Transactional
    public Medicine updateStock(Long id, Integer quantity) {
        log.info("Updating stock for medicine ID: {} with quantity: {}", id, quantity);
        Medicine medicine = getMedicineById(id);
        
        int newStock = medicine.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new InsufficientStockException("Insufficient stock for medicine: " + medicine.getName());
        }
        
        medicine.setStockQuantity(newStock);
        return medicineRepository.save(medicine);
    }
    
    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicineDetails) {
        log.info("Updating medicine with ID: {}", id);
        Medicine medicine = getMedicineById(id);
        
        medicine.setName(medicineDetails.getName());
        medicine.setGenericName(medicineDetails.getGenericName());
        medicine.setCategory(medicineDetails.getCategory());
        medicine.setManufacturer(medicineDetails.getManufacturer());
        medicine.setUnitPrice(medicineDetails.getUnitPrice());
        medicine.setReorderLevel(medicineDetails.getReorderLevel());
        medicine.setExpiryDate(medicineDetails.getExpiryDate());
        medicine.setDescription(medicineDetails.getDescription());
        medicine.setSideEffects(medicineDetails.getSideEffects());
        
        return medicineRepository.save(medicine);
    }
    
    @Transactional
    public void deleteMedicine(Long id) {
        log.info("Deleting medicine with ID: {}", id);
        Medicine medicine = getMedicineById(id);
        medicineRepository.delete(medicine);
    }
}
