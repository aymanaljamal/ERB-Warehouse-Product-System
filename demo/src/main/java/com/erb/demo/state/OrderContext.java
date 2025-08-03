package com.erb.demo.state;
import com.erb.demo.EmailService.EmailService;
import com.erb.demo.EmailService.EmailServiceClient;
import com.erb.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public class OrderContext {
    private OrderState state;
    private Order order;
    private EmailServiceClient emailServiceClient;;
    public OrderContext(Order order,EmailServiceClient emailServiceClient) {
        this.order = order;
        this.emailServiceClient =emailServiceClient;
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

    public  EmailServiceClient  getEmailService() {
        return emailServiceClient;
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
