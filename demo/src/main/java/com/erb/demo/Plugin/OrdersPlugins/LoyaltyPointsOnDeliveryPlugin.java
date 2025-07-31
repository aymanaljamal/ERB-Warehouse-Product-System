package com.erb.demo.Plugin.OrdersPlugins;

import com.erb.demo.model.Customer;
import com.erb.demo.model.Order;
import com.erb.demo.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyPointsOnDeliveryPlugin implements OrderPlugin {

    private final CustomerRepository customerRepository;

    public LoyaltyPointsOnDeliveryPlugin(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public boolean supports(Order order) {
        return order.getStatus() == Order.OrderStatus.DELIVERED;
    }

    @Override
    public void process(Order order) {
        Customer customer = order.getCustomer();
        int totalPoints = order.getItems().stream()
                .mapToInt(item -> item.getQuantity() * 10)
                .sum();

        Integer currentPoints = customer.getLoyaltyPoints();
        if (currentPoints == null) currentPoints = 0;

        customer.setLoyaltyPoints(currentPoints + totalPoints);
        customerRepository.save(customer);
    }
}
