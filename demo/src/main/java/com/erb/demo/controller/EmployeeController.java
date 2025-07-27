package com.erb.demo.controller;
import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.dto.DTO.EmployeeDto;
import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.DeliveryEmployeeSummary;
import com.erb.demo.dto.DeliveryEmployeeWithOrdersDto;
import com.erb.demo.dto.OrderProductDto;
import com.erb.demo.model.Employee;
import com.erb.demo.model.Order;
import com.erb.demo.repository.EmployeeRepository;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('STAFF')")
    public List<Employee> getAll() {
        return service.getAll();
    }
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or #id == principal.id")
    public Employee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Employee create(@RequestBody @Valid Employee employee) {
        return service.save(employee);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or #id == principal.id")
    public Employee update(@PathVariable Long id, @RequestBody @Valid Employee updated) {
        Employee e = service.getById(id);
        if (e != null) {
            e.setName(updated.getName());
            e.setEmail(updated.getEmail());
            e.setSalary(updated.getSalary());
            e.setWorkHours(updated.getWorkHours());
            e.setRank(updated.getRank());
            return service.save(e);
        }
        return null;
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public Page<EmployeeSummaryProjection> getSummary(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("totalDelivered").descending());
        return service.getEmployeeSummary(pageable);
    }
    @GetMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DELIVERY', 'STAFF')")
    public ResponseEntity<Object> redirectToImage(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    try {
                        String imageUrl = employee.getImage();
                        if (imageUrl == null || imageUrl.isBlank()) {
                            return ResponseEntity.badRequest().build();
                        }

                        URI imageUri = URI.create(imageUrl);

                        return ResponseEntity.status(HttpStatus.FOUND)
                                .location(imageUri)
                                .build();
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/delivery-employees")
    public ResponseEntity<Page<DeliveryEmployeeWithOrdersDto>> getDeliveryEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String statusStr,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Order.OrderStatus status = null;
        if (statusStr != null) {
            try {
                status = Order.OrderStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }


        Page<Long> employeeIdsPage = employeeRepository.findDeliveryEmployeeIds(name, status, pageable);

        List<Long> uniqueEmployeeIds = employeeIdsPage.getContent().stream().distinct().toList();

        final Order.OrderStatus finalStatus = status;
        List<DeliveryEmployeeWithOrdersDto> dtos = uniqueEmployeeIds.stream().map(id -> {
            Employee emp = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
            List<OrderDto> orders = orderRepository.findOrderDtosByDeliveredByIdAndOptionalStatus(id, finalStatus);
            return new DeliveryEmployeeWithOrdersDto(emp.getId(), emp.getName(), orders);
        }).toList();

        Page<DeliveryEmployeeWithOrdersDto> dtoPage = new PageImpl<>(dtos, pageable, employeeIdsPage.getTotalElements());

        return ResponseEntity.ok(dtoPage);

    }

    @GetMapping("/orders/{orderId}/products")
    public ResponseEntity<List<OrderProductDto>> getOrderProducts(@PathVariable Long orderId) {
        List<OrderProductDto> products = orderRepository.findProductsByOrderId(orderId);
        return ResponseEntity.ok(products);
    }

}



