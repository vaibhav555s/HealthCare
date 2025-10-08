package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hospitalapi.entity.Referral;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
