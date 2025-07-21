package com.erb.demo.service;

import com.erb.demo.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    Product save(Product product);
    void delete(Long id);
}
