package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Bed;
import com.hospital.hospitalapi.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    Optional<Bed> findByBedNumber(String bedNumber);
    List<Bed> findByWard(Ward ward);
    List<Bed> findByStatus(String status);
    List<Bed> findByWardAndStatus(Ward ward, String status);
}
