package com.erb.demo.state;

import com.erb.demo.model.Order;

public class CreatedState implements OrderState {
    private final OrderContext context;
    public CreatedState(OrderContext context) {
        this.context = context;
    }
    @Override
    public void next(OrderContext context) {
        context.setState(new ProcessingState(context));
    }
    @Override
    public void cancel(OrderContext context) {
        context.setState(new CancelledState(context));
    }
    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.CREATED;
    }
    @Override
    public void onEnter() {
        Order order = context.getOrder();
        context.getEmailService().sendOrderStatusEmail(order);
    }
}
