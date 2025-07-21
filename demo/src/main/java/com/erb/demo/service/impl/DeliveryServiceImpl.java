package com.erb.demo.service.impl;

import com.erb.demo.dto.EmployeeSummaryDTO;
import com.erb.demo.model.Delivery;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    @Override
    public List<Delivery> getAll() {
        return repository.findAll();
    }

    @Override
    public Delivery getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Delivery save(Delivery delivery) {
        return repository.save(delivery);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
