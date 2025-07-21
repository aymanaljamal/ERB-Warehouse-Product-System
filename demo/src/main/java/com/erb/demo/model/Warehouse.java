package com.erb.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Warehouse name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters (ISO)")
    @Column(name = "country_code")
    private String countryCode;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Created date must be in the past or present")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "warehouse")
    @JsonManagedReference(value = "warehouse-stock")
    private List<StockReceipt> stockReceipts;

    @OneToMany(mappedBy = "warehouse")
    private List<Employee> employees;
}
