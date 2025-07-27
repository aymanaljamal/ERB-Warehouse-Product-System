package com.erb.demo.repository;

import com.erb.demo.model.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    List<WarehouseProduct> findByProductIdAndQuantityGreaterThan(Long productId, int quantity);
    @Query("SELECT SUM(wp.quantity) FROM WarehouseProduct wp WHERE wp.product.id = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);
}
