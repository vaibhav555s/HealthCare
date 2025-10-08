package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Referral;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.repository.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReferralService {

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public Referral createReferral(Referral referral) {
        // Business Logic: Verify patient and both doctors exist
        Patient patient = patientRepository.findById(referral.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor referringDoctor = doctorRepository.findById(referral.getReferringDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Referring doctor not found"));
        Doctor referredToDoctor = doctorRepository.findById(referral.getReferredToDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Referred-to doctor not found"));

        referral.setPatient(patient);
        referral.setReferringDoctor(referringDoctor);
        referral.setReferredToDoctor(referredToDoctor);

        return referralRepository.save(referral);
    }

    @Transactional(readOnly = true)
    public Optional<Referral> getReferralById(Long id) {
        return referralRepository.findById(id);
    }

    @Transactional
    public Referral updateReferralStatus(Long id, String status) {
        Referral referral = referralRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referral not found"));
        
        referral.setStatus(status);
        return referralRepository.save(referral);
    }
}