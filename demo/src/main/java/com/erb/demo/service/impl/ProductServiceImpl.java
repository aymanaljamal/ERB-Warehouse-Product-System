package com.erb.demo.service.impl;

import com.erb.demo.dto.DTO.ProductStockDto;
import com.erb.demo.model.Product;
import com.erb.demo.model.WarehouseProduct;
import com.erb.demo.repository.ProductRepository;
import com.erb.demo.repository.WarehouseProductRepository;
import com.erb.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private WarehouseProductRepository warehouseProductRepository;
    @Autowired
    private ProductRepository repository;

    @Override
    @Cacheable(value = "products")
    public List<Product> getAll() {
        return repository.findAll();
    }
    public List<Product> getAllOrderedProducts() {
        return repository.findAllOrderedProducts();
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


    //=====================================================
    @Override
    public List<ProductStockDto> getAvailableProductsWithStock() {
        List<WarehouseProduct> stocks = warehouseProductRepository.findAll();
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (WarehouseProduct wp : stocks) {
            productQuantities.put(wp.getProduct().getId(),
                    productQuantities.getOrDefault(wp.getProduct().getId(), 0) + wp.getQuantity());
        }
        List<Product> products = repository.findAllById(productQuantities.keySet());
        return products.stream().map(p -> {
            ProductStockDto dto = new ProductStockDto();
            dto.setProductId(p.getId());
            dto.setProductName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setAvailableQuantity(productQuantities.getOrDefault(p.getId(), 0));
            return dto;
        }).collect(Collectors.toList());
    }
}
