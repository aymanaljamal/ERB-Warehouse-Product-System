package com.erb.demo.state;


import com.erb.demo.model.Order;

public class CreatedState implements OrderState {

    @Override
    public void next(OrderContext context) {
        context.setState(new ProcessingState());
    }

    @Override
    public void cancel(OrderContext context) {
        context.setState(new CancelledState());
    }

    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.CREATED;
    }
}
