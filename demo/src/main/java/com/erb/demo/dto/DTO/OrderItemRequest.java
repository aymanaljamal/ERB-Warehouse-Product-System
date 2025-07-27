package com.erb.demo.dto.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
