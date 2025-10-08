package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Billing;
import com.hospital.hospitalapi.repositorybean.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping
    public ResponseEntity<Billing> createBill(@RequestBody Billing billing) {
        try {
            return ResponseEntity.ok(billingService.createBill(billing));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillById(@PathVariable Long id) {
        return billingService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Billing> updatePaymentStatus(@PathVariable Long id, @RequestBody String status) {
        try {
            return ResponseEntity.ok(billingService.updatePaymentStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}