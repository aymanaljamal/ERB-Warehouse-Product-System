package com.erb.demo.testapi;

import com.erb.demo.dto.EmployeeDto;

import java.util.List;

public interface TestEmployeeService {
    List<EmployeeDto> getAll();
    EmployeeDto getById(Long id);
    EmployeeDto save(EmployeeDto dto);
    void delete(Long id);
}
