// Employee.java
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

    @Min(value = 0, message = "Salary must be positive")
    private double salary;

    @Min(value = 0, message = "Work hours must be positive")
    @Column(name = "work_hours")
    private int workHours;

    @Enumerated(EnumType.STRING)
    private Rank rank;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String image;

    @OneToMany(mappedBy = "employee")
    @JsonManagedReference(value = "employee-stock")
    private List<StockReceipt> stockReceipts;

    public enum Rank {
        STAFF,
        MANAGER
    }
}
