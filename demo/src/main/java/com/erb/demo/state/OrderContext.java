package com.erb.demo.state;
import com.erb.demo.EmailService.EmailService;
import com.erb.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public class OrderContext {
    private OrderState state;
    private Order order;
    private EmailService emailService;
    public OrderContext(Order order, EmailService emailService) {
        this.order = order;
        this.emailService = emailService;
        this.state = resolveState(order.getStatus());
    }

    private OrderState resolveState(@NotNull Order.OrderStatus status) {
        return switch (status) {
            case CREATED -> new CreatedState(this);
            case PROCESSING -> new ProcessingState(this);
            case SHIPPED -> new ShippedState(this);
            case DELIVERED -> new DeliveredState(this);
            case CANCELLED -> new CancelledState(this);
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setState(OrderState state) {
        this.state = state;
        order.setStatus(state.getStatus());
        state.onEnter();
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
