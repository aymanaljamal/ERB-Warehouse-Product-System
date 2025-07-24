package com.erb.demo.mapper;

import com.erb.demo.dto.EmployeeDto;
import com.erb.demo.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .salary(employee.getSalary())
                .workHours(employee.getWorkHours())

                .managerId(employee.getAdmin() != null ? employee.getAdmin().getId() : null)
                .build();
    }

    public Employee toEntity(EmployeeDto dto) {
        if (dto == null) return null;

        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setWorkHours(dto.getWorkHours());



        return employee;
    }
}
