package com.erb.demo.dto.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private Long productId;
    private int quantity;
}
