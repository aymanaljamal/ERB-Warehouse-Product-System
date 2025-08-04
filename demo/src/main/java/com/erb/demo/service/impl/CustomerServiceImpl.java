package com.erb.demo.service.impl;

import com.erb.demo.Plugin.CustomerPlugin.CustomerPluginExecutor;
import com.erb.demo.dto.DTO.CustomerDTO;
import com.erb.demo.model.Customer;
import com.erb.demo.repository.CustomerRepository;
import com.erb.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;
    @Autowired
    private CustomerPluginExecutor pluginExecutor;
    @Override
    @Cacheable(value = "customers")
    public List<Customer> getAll() {
        return repository.findAll();
    }
    @Override
    @Cacheable(value = "customers", key = "#id")
    public Customer getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "customers", key = "#customer.id")
    public Customer save(Customer customer) {
        Customer savedCustomer = repository.save(customer);
        pluginExecutor.execute(savedCustomer);
        return savedCustomer;
    }
    @Override
    @CacheEvict(value = "customers", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Customer getByEmail(String email) {
        return repository.findByEmail(email);
    }
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(c -> new CustomerDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getPhone()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = repository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found");
        }
        return customer;
    }
}
