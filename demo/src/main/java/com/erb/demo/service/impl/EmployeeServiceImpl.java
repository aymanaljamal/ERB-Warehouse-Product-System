package com.erb.demo.service.impl;

import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.dto.EmployeeSummaryDTO;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.repository.EmployeeRepository;
import com.erb.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
    @Override
    public Page<EmployeeSummaryProjection> getEmployeeSummary(Pageable pageable) {
        return deliveryRepository.getEmployeeSummary(pageable);
    }


}
