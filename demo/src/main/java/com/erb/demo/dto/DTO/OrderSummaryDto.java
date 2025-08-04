package com.erb.demo.dto.DTO;

import com.erb.demo.model.Order;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;

    public OrderSummaryDto(Order order) {
        this.id = order.getId();
        this.status = String.valueOf(order.getStatus());
        this.createdAt = order.getCreatedAt();
    }
}
