package com.erb.demo.dto;

public record OrderProductDto(
        Long productId,
        String productName,
        String description,
        double price,
        int quantity
) {}
