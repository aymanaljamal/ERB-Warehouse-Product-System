package com.erb.demo.state;

import com.erb.demo.model.Order;

public class CancelledState implements OrderState {

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
}
