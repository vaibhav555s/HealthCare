package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Referral;
import com.hospital.hospitalapi.repositorybean.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    @PostMapping
    public ResponseEntity<Referral> createReferral(@RequestBody Referral referral) {
        try {
            return ResponseEntity.ok(referralService.createReferral(referral));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referral> getReferralById(@PathVariable Long id) {
        return referralService.getReferralById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Referral> updateReferralStatus(@PathVariable Long id, @RequestBody String status) {
        try {
            return ResponseEntity.ok(referralService.updateReferralStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}