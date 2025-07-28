package com.erb.demo.state;

import com.erb.demo.model.Order;

public class ProcessingState implements OrderState {

    private final OrderContext context;
    public ProcessingState(OrderContext context) {
        this.context = context;
    }
    @Override
    public void next(OrderContext context) {
        context.setState(new ShippedState(context));
    }

    @Override
    public void cancel(OrderContext context) {
        context.setState(new CancelledState(context));
    }
    @Override
    public Order.OrderStatus getStatus() {
        return Order.OrderStatus.PROCESSING;
    }
    @Override
    public void onEnter() {
        Order order = context.getOrder();
        context.getEmailService().sendOrderStatusEmail(order);
    }
}
