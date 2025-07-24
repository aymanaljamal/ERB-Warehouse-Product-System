package com.erb.demo.controller;
import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.EmployeeRepository;
import com.erb.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;
    @Autowired
    private EmployeeRepository employeeRepository;
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
            e.setAdmin(updated.getAdmin());
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
    @GetMapping("/{id}/image")
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

}
