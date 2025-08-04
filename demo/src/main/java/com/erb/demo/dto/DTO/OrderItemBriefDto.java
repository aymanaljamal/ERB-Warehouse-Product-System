package com.erb.demo.dto.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemBriefDto {
    private Long orderItemId;
    private String orderItemName;
    private Integer quantity;
    private Long orderId;
    private String status;
}
