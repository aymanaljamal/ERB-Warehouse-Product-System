package com.erb.demo.dto;

import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryEmployeeWithOrdersDto {
    private Long employeeId;
    private String employeeName;
    private List<OrderDto> orders;

    public DeliveryEmployeeWithOrdersDto(Long employeeId, String employeeName, List<OrderDto> orders) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.orders = orders;
    }


}
