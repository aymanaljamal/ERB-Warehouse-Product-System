package com.erb.demo.dto.DTO;
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
}
