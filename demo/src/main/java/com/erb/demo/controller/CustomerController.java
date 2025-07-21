package com.erb.demo.controller;
import com.erb.demo.model.Customer;
import com.erb.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping
    public List<Customer> getAll() {
        logger.info("Fetching all customers...");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        logger.info("Fetching customer with ID: {}", id);
        return service.getById(id);
    }

    @PostMapping
    public Customer create(@RequestBody @Valid Customer customer) {
        logger.info("Creating new customer: {}", customer.getEmail());
        return service.save(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody @Valid Customer updated) {
        logger.info("Updating customer with ID: {}", id);
        Customer existing = service.getById(id);
        if (existing != null) {
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setAddress(updated.getAddress());
            return service.save(existing);
        } else {
            logger.warn("Customer with ID {} not found for update.", id);
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting customer with ID: {}", id);
        service.delete(id);
    }
}
