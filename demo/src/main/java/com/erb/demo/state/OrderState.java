package com.erb.demo.state;

import com.erb.demo.model.Order;

public interface OrderState {
    void next(OrderContext context);
    void cancel(OrderContext context);
    Order.OrderStatus getStatus();
    default void onEnter() {
    }
}
