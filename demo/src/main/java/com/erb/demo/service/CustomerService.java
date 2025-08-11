package com.erb.demo.service;

import com.erb.demo.dto.CustomerResponseDTO;
import com.erb.demo.dto.CustomerSearchCriteria;
import com.erb.demo.dto.DTO.CustomerDTO;
import com.erb.demo.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();
    Customer getById(Long id);
    Customer save(Customer customer);
    void delete(Long id);
    Customer getByEmail(String email);
    List<CustomerDTO> getAllCustomers();
    Customer getCurrentCustomer();
    List<CustomerResponseDTO> searchCustomers(CustomerSearchCriteria criteria);

}
