package com.erb.demo.dto.DTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    private List<OrderItemRequest> items;
}
