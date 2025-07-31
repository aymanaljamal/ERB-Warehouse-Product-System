package com.erb.demo.Plugin.OrdersPlugins;

import com.erb.demo.Plugin.OrdersPlugins.OrderPlugin;
import com.erb.demo.model.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class InternationalDeliveryPlugin implements OrderPlugin {

    @Override
    public boolean supports(Order order) {
        return order.getCustomer().getAddress().contains("International");
    }

    @Override
    public void process(Order order) {
        System.out.println("🌍 InternationalDeliveryPlugin applied to order #" + order.getId());
        order.setDeliveredAt(LocalDateTime.now().plusDays(7));
    }
}
