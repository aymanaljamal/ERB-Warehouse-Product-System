package com.erb.demo.dto.DTO;
import com.erb.demo.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private Order.OrderStatus status;
}
