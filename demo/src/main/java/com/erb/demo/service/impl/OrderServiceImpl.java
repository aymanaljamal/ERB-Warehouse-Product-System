package com.erb.demo.service.impl;

import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.DTO.OrderItemDto;
import com.erb.demo.model.Order;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return repository.findAll(pageable);
    }
    @Override
    public List<OrderDto> getAllOrders() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .items(order.getItems() != null ? order.getItems().stream()
                        .map(item -> OrderItemDto.builder()
                                .id(item.getId())
                                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                                .quantity(item.getQuantity())
                                .build())
                        .toList() : List.of())
                .build();
    }

}
