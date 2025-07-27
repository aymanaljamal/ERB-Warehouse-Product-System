package com.erb.demo.state;
import com.erb.demo.model.Order;
public class DeliveredState implements OrderState {

    @Override
    public void next(OrderContext context) {
        throw new IllegalStateException("Order is already delivered; no next state");
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel an order that has been delivered");
    }

    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.DELIVERED;
    }
}
