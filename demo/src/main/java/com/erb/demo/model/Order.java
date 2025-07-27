package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status must not be null")
    private OrderStatus status;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Delivered date cannot be in the future")
    private LocalDateTime deliveredAt;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items;

    public enum OrderStatus {
        CREATED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", deliveredAt=" + deliveredAt +
                ", customerId=" + (customer != null ? customer.getId() : null) +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                '}';
    }
}
