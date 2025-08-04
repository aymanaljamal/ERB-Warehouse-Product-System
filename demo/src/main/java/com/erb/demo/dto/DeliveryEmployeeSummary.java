package com.erb.demo.dto;

import com.erb.demo.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryEmployeeSummary {
    private  Long orderCount;
    private Long employeeId;
    private String employeeName;
    private Long undeliveredCount;
    private List<Order> orders;
    public DeliveryEmployeeSummary(Long employeeId, String employeeName, Long orderCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.orderCount = orderCount;
    }
    public DeliveryEmployeeSummary(Long employeeId, String employeeName, Long undeliveredCount, List<Order> orders) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.undeliveredCount = undeliveredCount;
        this.orders = orders;
    }



}
