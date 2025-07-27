package com.erb.demo.state;

import com.erb.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public class OrderContext {
    private OrderState state;
    private final Order order;

    public OrderContext(Order order) {
        this.order = order;
        this.state = resolveState(order.getStatus());
    }

    private OrderState resolveState(Order.@NotNull(message = "Status must not be null") OrderStatus status) {
        switch (status) {
            case CREATED: return new CreatedState();
            case PROCESSING: return new ProcessingState();
            case SHIPPED: return new ShippedState();
            case DELIVERED: return new DeliveredState();
            case CANCELLED: return new CancelledState();
            default: throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

    public void setState(OrderState state) {
        this.state = state;
        order.setStatus(state.getStatus());
    }

    public void next() {
        state.next(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public Order.OrderStatus getStatus() {
        return state.getStatus();
    }

    public Order getOrder() {
        return order;
    }
}
