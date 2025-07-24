package com.erb.demo.repository;

import com.erb.demo.model.Product;
import com.erb.demo.model.StockReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockReceiptRepository extends JpaRepository<StockReceipt, Long> {
    @Query("SELECT DISTINCT sr.product FROM StockReceipt sr")
    List<Product> findAllDeliveredProducts();
}
