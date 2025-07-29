package com.erb.demo.service.impl;

import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.dto.DTO.EmployeeDto;
import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.DeliveryEmployeeWithOrdersDto;
import com.erb.demo.model.Employee;
import com.erb.demo.model.Order;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.repository.EmployeeRepository;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Cacheable(value = "employees")
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Cacheable(value = "employees", key = "#id")
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    @CachePut(value = "employees", key = "#employee.id")
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @CacheEvict(value = "employees", key = "#id")
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<EmployeeSummaryProjection> getEmployeeSummary(Pageable pageable) {
        return deliveryRepository.getEmployeeSummary(pageable);
    }
    @Override
    public EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .salary(employee.getSalary())
                .workHours(employee.getWorkHours())
                .rank(employee.getRank().name())
                .build();
    }

    @Override
    public Page<DeliveryEmployeeWithOrdersDto> getDeliveryEmployees(String name, String statusStr, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Order.OrderStatus status = null;
        if (statusStr != null) {
            try {
                status = Order.OrderStatus.valueOf(statusStr.toUpperCase());
                System.out.println("Parsed status: " + status);
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }
        Page<Long> employeeIdsPage;

        if (status == null) {

            employeeIdsPage = orderRepository.findDeliveryEmployeeIdsWithoutStatus(name, pageable);
        } else {
            employeeIdsPage = orderRepository.findDeliveryEmployeeIdsWithStatus(name, status, pageable);
        }
        List<Long> uniqueEmployeeIds = employeeIdsPage.getContent().stream().distinct().toList();
        List<Employee> employees = employeeRepository.findAllByIdIn(uniqueEmployeeIds);
        Map<Long, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, e -> e));
        Order.OrderStatus finalStatus = status;
        List<DeliveryEmployeeWithOrdersDto> dtos = uniqueEmployeeIds.stream().map(id -> {
            Employee emp = employeeMap.get(id);
            if (emp == null) {
                throw new RuntimeException("Employee not found with id: " + id);
            }
            List<OrderDto> orders = orderRepository.findOrderDtosByDeliveredByIdAndOptionalStatus(id, finalStatus);
            return new DeliveryEmployeeWithOrdersDto(emp.getId(), emp.getName(), orders);
        }).toList();

        return new PageImpl<>(dtos, pageable, employeeIdsPage.getTotalElements());
    }


    @Override
    public Employee getEmployeeIfStaff(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee;
    }


}