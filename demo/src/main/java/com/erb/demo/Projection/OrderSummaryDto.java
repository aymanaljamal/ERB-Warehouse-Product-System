package com.erb.demo.Projection;

import com.erb.demo.model.Order.OrderStatus;
import java.time.LocalDateTime;

public class OrderSummaryDto {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String customerName;
    private String deliveryEmployeeName;
    private String processedByEmployeeName;

    public OrderSummaryDto(Long orderId, OrderStatus status, LocalDateTime createdAt,
                           String customerName, String deliveryEmployeeName, String processedByEmployeeName) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.deliveryEmployeeName = deliveryEmployeeName;
        this.processedByEmployeeName = processedByEmployeeName;
    }
    public Long getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public String getDeliveryEmployeeName() { return deliveryEmployeeName; }
    public String getProcessedByEmployeeName() { return processedByEmployeeName; }
}
