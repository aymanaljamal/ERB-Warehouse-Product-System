package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Received date and time is required")
    private LocalDateTime receivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-stock")
    @NotNull(message = "Employee is required")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    @JsonBackReference(value = "orderItem-stock")
    @NotNull(message = "Order Item is required")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference(value = "warehouse-stock")
    @NotNull(message = "Warehouse is required")
    private Warehouse warehouse;
}
