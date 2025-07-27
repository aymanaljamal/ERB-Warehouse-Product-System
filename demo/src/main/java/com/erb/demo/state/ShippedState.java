package com.erb.demo.state;

import com.erb.demo.model.Order;

import java.time.LocalDateTime;

public class ShippedState implements OrderState {

    @Override
    public void next(OrderContext context) {
        Order order = context.getOrder();
        order.setDeliveredAt(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.DELIVERED);
        context.setState(new DeliveredState());
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel an order that has already been shipped");
    }
    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.SHIPPED;
    }
}
