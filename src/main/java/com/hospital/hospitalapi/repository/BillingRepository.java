package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hospitalapi.model.Billing;

public interface BillingRepository extends JpaRepository<Billing, Long> {
}
