package com.erb.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {

    private Long id;
    private String name;
    private String email;
    private double salary;
    private int workHours;
    private String rank;

    private Long managerId;
}
