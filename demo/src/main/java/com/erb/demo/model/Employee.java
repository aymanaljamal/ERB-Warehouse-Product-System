package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"admin", "warehouse", "stockReceipts", "deliveries"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @Min(value = 0, message = "Salary must be positive")
    private double salary;

    @Min(value = 0, message = "Work hours must be positive")
    @Column(name = "work_hours")
    private int workHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private Rank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference(value = "warehouse-employees")
    private Warehouse warehouse;

    private String image;

    private String phone;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "employee-stock")
    private List<StockReceipt> stockReceipts;

    @OneToMany(mappedBy = "deliveredBy", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "employee-deliveries")
    private List<Delivery> deliveries;

    public enum Rank {
        SUPER_ADMIN,
        DELIVERY,
        STAFF
    }
    public Rank getRank() {
        return rank;
    }

    @OneToMany(mappedBy = "employee")
    private List<Order> orders;
}


