package com.erb.demo.dto.DTO;

import com.erb.demo.model.Employee;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeOrdersDto {
    private Long id;
    private String name;
    private String role;
    private List<OrderSummaryDto> orders;

    public EmployeeOrdersDto(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.role = String.valueOf(employee.getRank());
        this.orders = employee.getOrders().stream()
                .map(OrderSummaryDto::new)
                .toList();
    }
}