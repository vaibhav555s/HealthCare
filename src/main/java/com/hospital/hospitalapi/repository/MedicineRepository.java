package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByName(String name);
    List<Medicine> findByNameContainingIgnoreCase(String name);
    List<Medicine> findByCategory(String category);
    List<Medicine> findByStockQuantityLessThanEqual(Integer threshold);
    List<Medicine> findByIsActive(Boolean isActive);
}