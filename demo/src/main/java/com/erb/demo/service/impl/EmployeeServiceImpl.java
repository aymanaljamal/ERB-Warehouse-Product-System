package com.erb.demo.service.impl;

import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.dto.DTO.EmployeeDto;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.repository.EmployeeRepository;
import com.erb.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

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
}
