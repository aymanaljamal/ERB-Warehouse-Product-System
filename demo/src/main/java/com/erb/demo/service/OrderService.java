package com.erb.demo.service;

import com.erb.demo.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();
    Order getById(Long id);
    Order save(Order order);
    void delete(Long id);
}
