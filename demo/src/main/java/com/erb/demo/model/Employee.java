package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(name = "`rank`")
    private Rank rank;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Employee admin;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String image;

    private String phone;

    @OneToMany(mappedBy = "employee")
    @JsonManagedReference(value = "employee-stock")
    private List<StockReceipt> stockReceipts;

    public enum Rank {
        SUPER_ADMIN,
        DELIVERY,
        STAFF
    }
}
