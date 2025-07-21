package com.erb.demo.Projection;

import java.time.LocalDateTime;

public interface WarehouseBasicView {
    String getName();
    String getCountryCode();
    LocalDateTime getCreatedDate();
    Long getEmployeeCount();
    String getStatus();
}
