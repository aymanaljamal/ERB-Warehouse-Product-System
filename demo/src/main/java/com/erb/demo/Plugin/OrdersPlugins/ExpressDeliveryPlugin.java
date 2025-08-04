package com.erb.demo.Plugin.OrdersPlugins;
import com.erb.demo.model.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpressDeliveryPlugin implements OrderPlugin {

    @Override
    public boolean supports(Order order) {
        return order.getStatus() == Order.OrderStatus.PROCESSING &&
                order.getCustomer().getAddress().contains("CityCenter");
    }

    @Override
    public void process(Order order) {
        System.out.println("🔧 ExpressDeliveryPlugin applied: Expediting order #" + order.getId());
        order.setDeliveredAt(LocalDateTime.now().plusDays(1));
    }
}
