package com.erb.demo.controller;
import com.erb.demo.model.Customer;
import com.erb.demo.model.Employee;
import com.erb.demo.model.Order;
import com.erb.demo.model.Order.OrderStatus;
import com.erb.demo.service.CustomerService;
import com.erb.demo.service.EmployeeService;
import com.erb.demo.service.impl.OrderService;
import com.erb.demo.dto.OrderInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderGraphQLController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public OrderGraphQLController(OrderService orderService,
                                  CustomerService customerService,
                                  EmployeeService employeeService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @QueryMapping
    public List<Order> orders() {
        return orderService.getAllOrders();
    }

    @QueryMapping
    public Optional<Order> orderById(@Argument Long id) {
        return orderService.getOrderById(id);
    }

    @MutationMapping
    public Order createOrder(@Argument("input") OrderInput input) {

        Customer customer = customerService.getById(input.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + input.getCustomerId());
        }

        Employee employee = null;
        if (input.getEmployeeId() != null) {
            employee = employeeService.getById(input.getEmployeeId());
            if (employee == null) {
                throw new RuntimeException("Employee not found with ID: " + input.getEmployeeId());
            }
        }


        Order order = Order.builder()
                .status(OrderStatus.valueOf(input.getStatus().name()))
                .createdAt(LocalDateTime.parse(input.getCreatedAt()))
                .deliveredAt(input.getDeliveredAt() != null ? LocalDateTime.parse(input.getDeliveredAt()) : null)
                .customer(customer)
                .employee(employee)
                .build();

        return orderService.saveOrder(order);
    }
}
