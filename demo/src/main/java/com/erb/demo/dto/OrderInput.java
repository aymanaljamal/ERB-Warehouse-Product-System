package com.erb.demo.dto;

import com.erb.demo.model.Order.OrderStatus;
import lombok.Data;

@Data
public class OrderInput {
    private OrderStatus status;
    private String createdAt;
    private String deliveredAt;
    private Long customerId;
    private Long employeeId;
}
