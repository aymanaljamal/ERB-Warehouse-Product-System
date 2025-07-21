package com.erb.demo.service.impl;

import com.erb.demo.model.Product;
import com.erb.demo.repository.ProductRepository;
import com.erb.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    @Cacheable(value = "products")
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "products", key = "#product.id")
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
