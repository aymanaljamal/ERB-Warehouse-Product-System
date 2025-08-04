package com.erb.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private double totalPriceILS;
    private String orderApiLink;
    private List<OrderItemDTO> items;
}