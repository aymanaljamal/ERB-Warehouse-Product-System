package com.erb.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSearchCriteria {
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer minPoints;
    private Integer maxPoints;
}