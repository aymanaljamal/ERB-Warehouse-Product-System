package com.erb.demo.Projection;
public interface EmployeeSummaryProjection {
    Long getId();
    String getName();
    String getEmail();
    String getWarehouseName();
    int getTotalDelivered();
    String getStatus();
}