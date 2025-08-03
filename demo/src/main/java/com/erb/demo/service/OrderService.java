package com.erb.demo.service;

import com.erb.demo.Projection.OrderSummaryDto;
import com.erb.demo.dto.DTO.CreateOrderRequest;
import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.OrderDetailsDto;
import com.erb.demo.dto.OrderResponseDTO;
import com.erb.demo.model.Customer;
import com.erb.demo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<Order> getAll();
    Order getById(Long id);
    Order save(Order order);
    void delete(Long id);
    Page<Order> getAllOrders(Pageable pageable);
    List<OrderDto> getAllOrders();
    OrderDto mapToDto(Order order);
    OrderDto createOrder(CreateOrderRequest request);
    void updateOrderStatus(Long orderId, Order.OrderStatus status);
    OrderDetailsDto getOrderWithEmployee(Long id);
    List<OrderDetailsDto> getOrdersCreatedByStaff();
    Page<OrderSummaryDto> getOldUndeliveredOrders(LocalDateTime threeDaysAgo, Pageable pageable);
    Order processOrder(Long orderId);
    Page<OrderResponseDTO> getCustomerOrders(Customer customer, Pageable pageable, String baseUrl);


}

