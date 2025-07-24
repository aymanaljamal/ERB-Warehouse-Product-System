package com.erb.demo.controller;

import com.erb.demo.model.Order;
import com.erb.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public List<Order> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Order create(@RequestBody @Valid Order order) {
        order.setCreatedAt(LocalDateTime.now());
        return service.save(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody @Valid Order updated) {
        Order existing = service.getById(id);
        if (existing != null) {
            existing.setStatus(updated.getStatus());
            existing.setDeliveredAt(updated.getDeliveredAt());
            return service.save(existing);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<Page<Order>> getAllOrders(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<Order> ordersPage = service.getAllOrders(pageable);
        return ResponseEntity.ok(ordersPage);
    }
}
