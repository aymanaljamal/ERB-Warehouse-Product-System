package com.erb.demo.controller;

import com.erb.demo.companyProject.TimeIntervalHelper;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.DeliveryRepository;
import com.erb.demo.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private final DeliveryRepository deliveryRepository;
    private final EmployeeRepository employeeRepository;

    public SuperAdminController(DeliveryRepository deliveryRepository, EmployeeRepository employeeRepository) {
        this.deliveryRepository = deliveryRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/performance-review")
    public ResponseEntity<?> performanceReview(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam int raiseThreshold,
            @RequestParam int fireThreshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Date fromDate = TimeIntervalHelper.parseUtcTimestamp(from);
        Date toDate = TimeIntervalHelper.parseUtcTimestamp(to);

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<Map<String, Object>> raiseList = new ArrayList<>();
        List<Map<String, Object>> firedList = new ArrayList<>();
        List<Map<String, Object>> normalList = new ArrayList<>();

        for (Employee e : employeePage.getContent()) {
            int deliveredCount = deliveryRepository.countByDeliveredByAndDeliveredAtBetween(
                    e.getId(),
                    fromDate,
                    toDate
            );

            Map<String, Object> empData = Map.of(
                    "employeeId", e.getId(),
                    "name", e.getName(),
                    "deliveredOrders", deliveredCount
            );

            if (deliveredCount >= raiseThreshold) {
                raiseList.add(empData);
            } else if (deliveredCount <= fireThreshold) {
                firedList.add(empData);
            } else {
                normalList.add(empData);
            }
        }

        Map<String, Object> response = Map.of(
                "raise", raiseList,
                "fired", firedList,
                "normal", normalList,
                "page", employeePage.getNumber(),
                "size", employeePage.getSize(),
                "totalElements", employeePage.getTotalElements(),
                "totalPages", employeePage.getTotalPages(),
                "last", employeePage.isLast()
        );

        return ResponseEntity.ok(response);
    }
}