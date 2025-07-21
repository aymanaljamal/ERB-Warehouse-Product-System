package com.erb.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeSummaryDTO {
    private Long id;
    private String name;
    private String email;
    private String warehouseName;
    private int ordersDeliveredToday;
    private String status;
}
