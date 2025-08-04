package com.erb.demo.repository;

import com.erb.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT oi.product FROM OrderItem oi")
    List<Product> findAllOrderedProducts();
    @Query("SELECT SUM(wp.quantity) FROM WarehouseProduct wp WHERE wp.product.id = :productId")
    int getTotalQuantityByProductId(@Param("productId") Long productId);
}
