package com.erb.demo.dto;
import com.erb.demo.model.Order;
import lombok.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private String customerName;
    private String employeeName;

    public OrderDetailsDto(Order order) {
        this.id = order.getId();
        this.status = String.valueOf(order.getStatus());
        this.createdAt = order.getCreatedAt();
        this.customerName = order.getCustomer().getName();
        this.employeeName = order.getEmployee() != null ? order.getEmployee().getName() : null;
    }
}
