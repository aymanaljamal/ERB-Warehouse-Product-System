package com.erb.demo.dto.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductOrderItemsDto {
    private Long productId;
    private String productName;
    private List<OrderItemBriefDto> orderItems;
}

