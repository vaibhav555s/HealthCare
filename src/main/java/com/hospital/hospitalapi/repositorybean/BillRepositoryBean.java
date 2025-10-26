package com.hospital.hospitalapi.repositorybean;

import com.hospital.hospitalapi.entity.*;
import com.hospital.hospitalapi.repository.BillRepository;
import com.hospital.hospitalapi.repository.BillItemRepository;
import com.hospital.hospitalapi.exception.BillNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillRepositoryBean {
    
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final PatientRepositoryBean patientRepositoryBean;
    
    @Transactional
    public Bill createBill(Long patientId) {
        log.info("Creating bill for patient ID: {}", patientId);
        
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        
        Bill bill = new Bill();
        bill.setPatient(patient);
        bill.setBillNumber(generateBillNumber());
        bill.setBillDate(LocalDateTime.now());
        bill.setTotalAmount(0.0);
        bill.setPaidAmount(0.0);
        bill.setBalanceAmount(0.0);
        bill.setPaymentStatus("PENDING");
        
        Bill saved = billRepository.save(bill);
        log.info("Bill created with number: {}", saved.getBillNumber());
        return saved;
    }
    
    private String generateBillNumber() {
        return "BILL-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    @Transactional
    public BillItem addBillItem(BillItem item, Long billId) {
        log.info("Adding item to bill ID: {}", billId);
        
        Bill bill = getBillById(billId);
        item.setBill(bill);
        item.setTotalPrice(item.getUnitPrice() * item.getQuantity());
        
        BillItem saved = billItemRepository.save(item);
        
        // Update bill total
        bill.setTotalAmount(bill.getTotalAmount() + saved.getTotalPrice());
        bill.setBalanceAmount(bill.getTotalAmount() - bill.getPaidAmount());
        billRepository.save(bill);
        
        log.info("Bill item added with ID: {}", saved.getId());
        return saved;
    }
    
    @Transactional
    public Bill processPayment(Long billId, Double amount) {
        log.info("Processing payment for bill ID: {} amount: {}", billId, amount);
        
        Bill bill = getBillById(billId);
        bill.setPaidAmount(bill.getPaidAmount() + amount);
        bill.setBalanceAmount(bill.getTotalAmount() - bill.getPaidAmount());
        
        if (bill.getBalanceAmount() <= 0) {
            bill.setPaymentStatus("PAID");
        } else if (bill.getPaidAmount() > 0) {
            bill.setPaymentStatus("PARTIAL");
        }
        
        return billRepository.save(bill);
    }
    
    public Bill getBillById(Long id) {
        log.debug("Fetching bill by ID: {}", id);
        return billRepository.findById(id)
            .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));
    }
    
    public List<Bill> getBillsByPatient(Long patientId) {
        Patient patient = patientRepositoryBean.getPatientById(patientId);
        return billRepository.findByPatient(patient);
    }
    
    public List<Bill> getPendingBills() {
        return billRepository.findByPaymentStatus("PENDING");
    }
}

