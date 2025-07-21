package com.erb.demo.dto;

import com.erb.demo.model.Employee.Rank;
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
    private Rank rank;

    private Long managerId;
}
