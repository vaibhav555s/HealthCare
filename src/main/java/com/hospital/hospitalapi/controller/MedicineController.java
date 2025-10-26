package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.entity.Medicine;
import com.hospital.hospitalapi.dto.request.MedicineRequest;
import com.hospital.hospitalapi.dto.response.ApiResponse;
import com.hospital.hospitalapi.dto.response.MedicineResponse;
import com.hospital.hospitalapi.repositorybean.MedicineRepositoryBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Tag(name = "Medicine Inventory Management")
public class MedicineController {
    
    private final MedicineRepositoryBean medicineRepositoryBean;
    
    @PostMapping
    @Operation(summary = "Add new medicine")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(@RequestBody MedicineRequest request) {
        Medicine medicine = mapToEntity(request);
        Medicine created = medicineRepositoryBean.createMedicine(medicine);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Medicine added successfully", mapToResponse(created)));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getAllMedicines() {
        List<MedicineResponse> medicines = medicineRepositoryBean.getAllMedicines()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved", medicines));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineRepositoryBean.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved", mapToResponse(medicine)));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> searchMedicines(@RequestParam String name) {
        List<MedicineResponse> medicines = medicineRepositoryBean.searchMedicines(name)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search results", medicines));
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getLowStockMedicines(
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<MedicineResponse> medicines = medicineRepositoryBean.getLowStockMedicines(threshold)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Low stock medicines", medicines));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(
            @PathVariable Long id, @RequestBody MedicineRequest request) {
        Medicine medicine = mapToEntity(request);
        Medicine updated = medicineRepositoryBean.updateMedicine(id, medicine);
        return ResponseEntity.ok(ApiResponse.success("Medicine updated", mapToResponse(updated)));
    }
    
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateStock(
            @PathVariable Long id, @RequestParam Integer quantity) {
        Medicine updated = medicineRepositoryBean.updateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock updated", mapToResponse(updated)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        medicineRepositoryBean.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine deleted", null));
    }
    
    private Medicine mapToEntity(MedicineRequest request) {
        Medicine medicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setGenericName(request.getGenericName());
        medicine.setCategory(request.getCategory());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUnitPrice(request.getUnitPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setReorderLevel(request.getReorderLevel());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setDescription(request.getDescription());
        medicine.setSideEffects(request.getSideEffects());
        return medicine;
    }
    
    private MedicineResponse mapToResponse(Medicine medicine) {
        MedicineResponse response = new MedicineResponse();
        response.setId(medicine.getId());
        response.setName(medicine.getName());
        response.setGenericName(medicine.getGenericName());
        response.setCategory(medicine.getCategory());
        response.setManufacturer(medicine.getManufacturer());
        response.setUnitPrice(medicine.getUnitPrice());
        response.setStockQuantity(medicine.getStockQuantity());
        response.setReorderLevel(medicine.getReorderLevel());
        response.setExpiryDate(medicine.getExpiryDate());
        response.setDescription(medicine.getDescription());
        response.setIsActive(medicine.getIsActive());
        return response;
    }
}
