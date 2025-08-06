package com.erb.demo.dto.DTO;

import com.erb.demo.model.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSearchCriteria {

    private String customerName;

    private String employeeName;

    private OrderStatus orderStatus;

    private LocalDateTime deliveredAfter;

    private LocalDateTime deliveredBefore;

}
