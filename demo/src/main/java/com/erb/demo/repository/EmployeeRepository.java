package com.erb.demo.repository;
import com.erb.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
    List<Employee> findAllByIdIn(List<Long> ids);
}
