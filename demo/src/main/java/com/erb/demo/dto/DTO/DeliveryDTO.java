package com.erb.demo.dto.DTO;

import java.time.LocalDateTime;

public class DeliveryDTO {
    private Long id;
    private LocalDateTime deliveredAt;
    private Long orderId;
    private Long employeeId;


    public DeliveryDTO(Long id, LocalDateTime deliveredAt, Long orderId, Long employeeId) {
        this.id = id;
        this.deliveredAt = deliveredAt;
        this.orderId = orderId;
        this.employeeId = employeeId;
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
