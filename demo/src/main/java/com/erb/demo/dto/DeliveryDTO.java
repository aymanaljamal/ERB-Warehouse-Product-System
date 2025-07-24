package com.erb.demo.dto;
import java.time.LocalDateTime;
public class DeliveryDTO {
    private Long id;
    private LocalDateTime deliveredAt;
    private Long orderId; // معرف فقط، لا كائن كامل
    private Long employeeId; // معرف فقط، لا كائن كامل

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}