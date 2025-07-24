package com.erb.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Order is required")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull(message = "Product is required")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public double getPrice() {
        return 0;
    }
}
