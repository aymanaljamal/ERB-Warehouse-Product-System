package com.erb.demo.security;

import com.erb.demo.model.Customer;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.CustomerRepository;
import com.erb.demo.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GeneralUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public GeneralUserDetailsService(CustomerRepository customerRepository,
                                     EmployeeRepository employeeRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return new CustomerUserDetails(customer);
        }

        Employee employee = employeeRepository.findByEmail(email);
        if (employee != null) {
            return new EmployeeUserDetails(employee);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
