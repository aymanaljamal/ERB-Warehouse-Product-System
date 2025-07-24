package com.erb.demo.repository;

import com.erb.demo.model.Product;
import com.erb.demo.model.StockReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockReceiptRepository extends JpaRepository<StockReceipt, Long> {
    @Query("SELECT sr FROM StockReceipt sr WHERE sr.orderItem IS NOT NULL AND sr.orderItem.order IS NOT NULL")
    List<StockReceipt> findAllWhereOrderExists();
}
