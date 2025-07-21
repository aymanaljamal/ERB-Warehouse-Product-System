package com.erb.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Delivery date must not be null")
    @PastOrPresent(message = "Delivery date cannot be in the future")
    private LocalDateTime deliveredAt;

    @NotNull(message = "Order must be provided")
    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @NotNull(message = "DeliveredBy (employee) is required")
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee deliveredBy;
}
