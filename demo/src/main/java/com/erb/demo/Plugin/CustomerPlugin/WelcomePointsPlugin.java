package com.erb.demo.Plugin.CustomerPlugin;

import com.erb.demo.Plugin.CustomerPlugin.CustomerPlugin;
import com.erb.demo.model.Customer;
import com.erb.demo.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class WelcomePointsPlugin implements CustomerPlugin {

    private final CustomerRepository customerRepository;

    public WelcomePointsPlugin(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean supports(Customer customer) {
        return customer.getId() == null;
    }

    @Override
    public void process(Customer customer) {
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + 50);
        customerRepository.save(customer);
    }
}
