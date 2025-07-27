package com.erb.demo.dto.DTO;
import lombok.Data;

@Data
public class ProductStockDto {
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int availableQuantity;
}


