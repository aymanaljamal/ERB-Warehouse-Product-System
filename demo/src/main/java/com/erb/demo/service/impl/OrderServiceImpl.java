package com.erb.demo.service.impl;

import com.erb.demo.model.Order;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository;

    @Override
    @Cacheable(value = "orders")
    public List<Order> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Order getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "orders", key = "#order.id")
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
