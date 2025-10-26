package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Bill;
import com.hospital.hospitalapi.entity.BillItem;
import com.hospital.hospitalapi.dto.request.BillRequest;
import com.hospital.hospitalapi.dto.request.BillItemRequest;
import com.hospital.hospitalapi.dto.request.PaymentRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.BillResponse;
import com.hospital.hospitalapi.dto.response.BillItemResponse;
import com.hospital.hospitalapi.repositorybean.BillRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@Tag(name = "Billing & Payments")
public class BillController {
    
    private final BillRepositoryBean billRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Generate bill")
    public ResponseEntity<ApiResponse<BillResponse>> createBill(@RequestBody BillRequest request) {
        Bill bill = billRepositoryBean.createBill(request.getPatientId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Bill generated", mapToResponse(bill)));
    }
    
    @PostMapping("/{id}/items")
    @Operation(summary = "Add bill item")
    public ResponseEntity<ApiResponse<BillItemResponse>> addBillItem(
            @PathVariable Long id, @RequestBody BillItemRequest request) {
        BillItem item = mapItemToEntity(request);
        BillItem created = billRepositoryBean.addBillItem(item, id);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Item added to bill", mapItemToResponse(created)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> getBillById(@PathVariable Long id) {
        Bill bill = billRepositoryBean.getBillById(id);
        return ResponseEntity.ok(ApiResponse.success("Bill retrieved", mapToResponse(bill)));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getBillsByPatient(@PathVariable Long patientId) {
        List<BillResponse> bills = billRepositoryBean.getBillsByPatient(patientId)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Bills retrieved", bills));
    }
    
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getPendingBills() {
        List<BillResponse> bills = billRepositoryBean.getPendingBills()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Pending bills retrieved", bills));
    }
    
    @PostMapping("/{id}/payments")
    @Operation(summary = "Process payment")
    public ResponseEntity<ApiResponse<BillResponse>> processPayment(
            @PathVariable Long id, @RequestBody PaymentRequest request) {
        Bill bill = billRepositoryBean.processPayment(id, request.getAmount());
        return ResponseEntity.ok(ApiResponse.success("Payment processed", mapToResponse(bill)));
    }
    
    private BillItem mapItemToEntity(BillItemRequest request) {
        BillItem item = new BillItem();
        item.setItemType(request.getItemType());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        return item;
    }
    
    private BillResponse mapToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setPatientName(bill.getPatient().getFirstName() + " " + bill.getPatient().getLastName());
        response.setBillNumber(bill.getBillNumber());
        response.setBillDate(bill.getBillDate());
        response.setTotalAmount(bill.getTotalAmount());
        response.setPaidAmount(bill.getPaidAmount());
        response.setBalanceAmount(bill.getBalanceAmount());
        response.setPaymentStatus(bill.getPaymentStatus());
        
        if (bill.getItems() != null) {
            List<BillItemResponse> items = bill.getItems().stream()
                .map(this::mapItemToResponse).collect(Collectors.toList());
            response.setItems(items);
        }
        
        return response;
    }
    
    private BillItemResponse mapItemToResponse(BillItem item) {
        BillItemResponse response = new BillItemResponse();
        response.setId(item.getId());
        response.setItemType(item.getItemType());
        response.setDescription(item.getDescription());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        return response;
    }
}
