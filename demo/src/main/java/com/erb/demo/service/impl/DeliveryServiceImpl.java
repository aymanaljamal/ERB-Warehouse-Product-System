package com.erb.demo.service.impl;

import com.erb.demo.model.Delivery;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    @Override
    @Cacheable(value = "deliveries")
    public List<Delivery> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "deliveries", key = "#id")
    public Delivery getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "deliveries", key = "#delivery.id")
    public Delivery save(Delivery delivery) {
        return repository.save(delivery);
    }

    @Override
    @CacheEvict(value = "deliveries", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
