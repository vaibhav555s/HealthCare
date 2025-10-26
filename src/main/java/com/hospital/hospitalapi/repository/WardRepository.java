package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    Optional<Ward> findByWardNumber(String wardNumber);
    List<Ward> findByWardType(String wardType);
    List<Ward> findByIsActive(Boolean isActive);
}