package com.erb.demo.controller;
import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.dto.EmployeeSummaryDTO;
import com.erb.demo.model.Employee;
import com.erb.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Employee create(@RequestBody @Valid Employee employee) {
        return service.save(employee);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody @Valid Employee updated) {
        Employee e = service.getById(id);
        if (e != null) {
            e.setName(updated.getName());
            e.setEmail(updated.getEmail());
            e.setSalary(updated.getSalary());
            e.setWorkHours(updated.getWorkHours());
            e.setRank(updated.getRank());
            e.setManager(updated.getManager());
            return service.save(e);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/summary")
    public Page<EmployeeSummaryProjection> getSummary(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("totalDelivered").descending());
        return service.getEmployeeSummary(pageable);
    }

}
