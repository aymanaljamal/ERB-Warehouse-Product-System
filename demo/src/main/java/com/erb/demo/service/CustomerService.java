package com.erb.demo.service;

import com.erb.demo.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();
    Customer getById(Long id);
    Customer save(Customer customer);
    void delete(Long id);
}
