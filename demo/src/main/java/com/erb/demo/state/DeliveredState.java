package com.erb.demo.state;

import com.erb.demo.model.Order;

public class DeliveredState implements OrderState {
    private final OrderContext context;
    public DeliveredState(OrderContext context) {
        this.context = context;
    }
    @Override
    public void next(OrderContext context) {
        throw new IllegalStateException("Order is already delivered; no next state.");
    }
    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Delivered order cannot be cancelled.");
    }
    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.DELIVERED;
    }
    @Override
    public void onEnter() {
        Order order = context.getOrder();
        context.getEmailService().sendOrderStatusEmail(order);
    }

}
