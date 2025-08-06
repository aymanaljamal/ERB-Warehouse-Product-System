package com.erb.demo.repository;

import com.erb.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> , JpaSpecificationExecutor<Customer> {
    Customer findByEmail(String email);

}
