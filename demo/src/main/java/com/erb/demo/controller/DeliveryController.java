package com.erb.demo.controller;

import com.erb.demo.dto.DTO.DeliveryDTO;
import com.erb.demo.model.Delivery;
import com.erb.demo.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService service;


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY')")
    public List<DeliveryDTO> getAllDeliveries() {
        return service.getAllDeliveries();
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY')")
    public List<Delivery> getAll() {
        return service.getAll();
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY')")
    @GetMapping("/{id}")
    public Delivery getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY')")
    @PostMapping
    public Delivery create(@RequestBody Delivery delivery) {
        delivery.setDeliveredAt(LocalDateTime.now());
        return service.save(delivery);
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY')")
    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id, @RequestBody @Valid Delivery updated) {
        Delivery existing = service.getById(id);
        if (existing != null) {
            existing.setDeliveredAt(updated.getDeliveredAt());
            existing.setDeliveredBy(updated.getDeliveredBy());
            return service.save(existing);
        }
        return null;
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
