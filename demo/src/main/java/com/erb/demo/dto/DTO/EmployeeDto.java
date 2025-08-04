package com.erb.demo.dto.DTO;

import lombok.*;

@Getter
@Setter
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
}
