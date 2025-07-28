package com.erb.demo.state;

import com.erb.demo.model.Order;

public class ShippedState implements OrderState {
    private final OrderContext context;
    public ShippedState(OrderContext context) {
        this.context = context;
    }

    @Override
    public void next(OrderContext context) {
        context.setState(new DeliveredState(context));
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel order after it has been shipped");
    }

    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.SHIPPED;
    }
    @Override
    public void onEnter() {
        Order order = context.getOrder();
        context.getEmailService().sendOrderStatusEmail(order);
    }
}
