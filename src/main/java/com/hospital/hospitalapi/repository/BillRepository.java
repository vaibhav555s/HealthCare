package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Bill;
import com.hospital.hospitalapi.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);
    List<Bill> findByPatient(Patient patient);
    List<Bill> findByPaymentStatus(String paymentStatus);
}
