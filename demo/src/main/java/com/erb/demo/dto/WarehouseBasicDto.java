package com.erb.demo.dto;

import java.time.LocalDateTime;

public class WarehouseBasicDto {
    private String name;
    private String countryCode;
    private LocalDateTime createdDate;

    public WarehouseBasicDto(String name, String countryCode, LocalDateTime createdDate) {
        this.name = name;
        this.countryCode = countryCode;
        this.createdDate = createdDate;
    }

    // getters and setters
}
