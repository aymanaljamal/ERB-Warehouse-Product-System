package com.erb.demo.service;

import com.erb.demo.Projection.EmployeeSummaryProjection;

import com.erb.demo.dto.DTO.EmployeeDto;
import com.erb.demo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface EmployeeService {
    List<Employee> getAll();
    Employee getById(Long id);
    Employee save(Employee employee);
    void delete(Long id);
    Page<EmployeeSummaryProjection> getEmployeeSummary(Pageable pageable);
    EmployeeDto mapToDto(Employee employee);
    List<EmployeeDto> getAllEmployees();
}
