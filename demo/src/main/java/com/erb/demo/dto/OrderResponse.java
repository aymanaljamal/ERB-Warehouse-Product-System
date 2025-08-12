package com.erb.demo.dto;

import com.erb.demo.model.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    private String customerName;

    private String employeeName;
}
