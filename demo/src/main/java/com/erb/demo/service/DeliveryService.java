package com.erb.demo.service;

import com.erb.demo.dto.DTO.DeliveryDTO;
import com.erb.demo.model.Delivery;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getAll();
    Delivery getById(Long id);
    Delivery save(Delivery delivery);
    void delete(Long id);
    List<DeliveryDTO> getAllDeliveries();
}
