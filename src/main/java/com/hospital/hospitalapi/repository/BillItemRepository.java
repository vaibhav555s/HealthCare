package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.BillItem;
import com.hospital.hospitalapi.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    List<BillItem> findByBill(Bill bill);
}

