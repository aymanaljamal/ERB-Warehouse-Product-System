package com.erb.demo.Plugin.OrdersPlugins;

import  com.erb.demo.model.Order;

public interface OrderPlugin {
    boolean supports(Order order);
    void process(Order order);

}
