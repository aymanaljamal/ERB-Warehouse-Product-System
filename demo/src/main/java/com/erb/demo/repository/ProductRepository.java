package com.erb.demo.repository;

import com.erb.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT oi.product FROM OrderItem oi")
    List<Product> findAllOrderedProducts();
}
