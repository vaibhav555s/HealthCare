package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.Billing;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.repository.BillingRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public Billing createBill(Billing billing) {
        Patient patient = patientRepository.findById(billing.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        billing.setPatient(patient);
        return billingRepository.save(billing);
    }

    @Transactional(readOnly = true)
    public Optional<Billing> getBillById(Long id) {
        return billingRepository.findById(id);
    }

    @Transactional
    public Billing updatePaymentStatus(Long id, String status) {
        Billing bill = billingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        bill.setPaymentStatus(status);
        return billingRepository.save(bill);
    }
}