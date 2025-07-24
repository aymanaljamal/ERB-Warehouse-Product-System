package com.erb.demo.service;

import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> getAll();
    Order getById(Long id);
    Order save(Order order);
    void delete(Long id);
    Page<Order> getAllOrders(Pageable pageable);

    List<OrderDto> getAllOrders();

    OrderDto mapToDto(Order order);
}

