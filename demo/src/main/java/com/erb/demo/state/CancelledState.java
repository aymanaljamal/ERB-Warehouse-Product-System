package com.erb.demo.state;

import com.erb.demo.model.Order;

public class CancelledState implements OrderState {
    private final OrderContext context;

    public CancelledState(OrderContext context) {
        this.context = context;
    }

    @Override
    public void next(OrderContext context) {
        throw new IllegalStateException("Cancelled order cannot progress to next state");
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Order is already cancelled");
    }

    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.CANCELLED;
    }
    @Override
    public void onEnter() {
        Order order = context.getOrder();
        context.getEmailService().sendOrderStatusEmail(order);
    }
}
