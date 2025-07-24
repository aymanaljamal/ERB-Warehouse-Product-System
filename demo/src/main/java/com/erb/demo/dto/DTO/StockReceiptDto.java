package com.erb.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReceiptDto {
    private Long id;
    private int quantity;
    private LocalDateTime receivedAt;

    private Long employeeId;
    private String employeeName;

    private Long orderItemId;

    private Long productId;
    private String productName;

    private Long warehouseId;
    private String warehouseLocation;

    private String orderItemName;
}
