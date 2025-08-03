package com.erb.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private String productName;
    private int quantity;
    private double priceUSD;
}