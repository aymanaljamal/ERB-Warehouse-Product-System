package com.erb.demo.dto.DTO;
import com.erb.demo.model.Order;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private Long customerId;
    private List<OrderItemDto> items;
    public OrderDto(Long id, Order.OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.status = status.name();
        this.createdAt = createdAt;
    }
    public OrderDto(Long id, Order.OrderStatus status, LocalDateTime createdAt, LocalDateTime deliveredAt, Long customerId) {
        this.id = id;
        this.status = String.valueOf(status);
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.customerId = customerId;
    }
}
