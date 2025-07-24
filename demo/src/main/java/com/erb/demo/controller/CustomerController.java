package com.erb.demo.controller;
import com.erb.demo.model.Customer;
import com.erb.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public List<Customer> getAll() {
        logger.info("Fetching all customers...");
        return service.getAll();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF', 'CUSTOMER')")
    public Customer getById(@PathVariable Long id, Authentication authentication) {
        logger.info("Fetching customer with ID: {}", id);

        String currentUserEmail = authentication.getName();
        logger.info("Authenticated user email: {}", currentUserEmail);

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Customer loggedInCustomer = service.getByEmail(currentUserEmail);
            if (loggedInCustomer == null) {
                logger.warn("Logged-in customer not found by email: {}", currentUserEmail);
                throw new AccessDeniedException("Authenticated customer not found");
            }
            logger.info("Logged-in customer ID: {}", loggedInCustomer.getId());

            if (!loggedInCustomer.getId().equals(id)) {
                logger.warn("Access denied: user ID {} tried to access customer ID {}", loggedInCustomer.getId(), id);
                throw new AccessDeniedException("You can only access your own customer data.");
            }
        }
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Customer create(@RequestBody @Valid Customer customer) {
        logger.info("Creating new customer: {}", customer.getEmail());
        return service.save(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
    public Customer update(@PathVariable Long id,
                           @RequestBody @Valid Customer updated,
                           Authentication authentication) {
        logger.info("Updating customer with ID: {}", id);
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {

            Customer loggedIn = service.getByEmail(authentication.getName());

            if (loggedIn == null || !loggedIn.getId().equals(id)) {
                logger.warn("Customer ID {} is not authorized to update this account", id);
                throw new AccessDeniedException("You can only update your own account.");
            }
        }
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting customer with ID: {}", id);
        service.delete(id);
    }
}
