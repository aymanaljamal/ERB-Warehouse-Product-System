package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "delivery")
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee deliveredBy;


    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", deliveredAt=" + deliveredAt +
                ", orderId=" + (order != null ? order.getId() : null) +
                ", deliveredById=" + (deliveredBy != null ? deliveredBy.getId() : null) +
                '}';
    }
}
