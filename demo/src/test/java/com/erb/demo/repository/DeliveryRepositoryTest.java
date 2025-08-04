package com.erb.demo.repository;
import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.model.Delivery;
import com.erb.demo.model.Employee;
import com.erb.demo.model.Order;
import com.erb.demo.model.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EmployeeSummaryProjection projection;

    @Test
    public void testGetEmployeeSummary() {


        Warehouse w = warehouseRepository.save(Warehouse.builder().name("Main Warehouse").build());

        Employee e = employeeRepository.save(Employee.builder()
                .name("John Doe")
                .email("john@example.com")
                .warehouse(w)
                .build());

        Delivery d1 = Delivery.builder()
                .deliveredBy(e)
                .deliveredAt(LocalDateTime.now().minusDays(1))
                .order(new Order())
                .build();

        deliveryRepository.save(d1);

        PageRequest pageable = PageRequest.of(0, 5);
        Page<EmployeeSummaryProjection> result = deliveryRepository.getEmployeeSummary(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();

        EmployeeSummaryProjection summary = result.getContent().get(0);

        assertThat(summary.getId()).isNotNull();
        assertThat(summary.getName()).isNotNull();
        assertThat(summary.getEmail()).isNotNull();
        assertThat(summary.getWarehouseName()).isNotNull();
        assertThat(summary.getTotalDelivered()).isNotNull();
        assertThat(summary.getStatus()).isIn("needs_raise", "normal");

        System.out.println("Employee name: " + summary.getName());
        System.out.println("Total delivered: " + summary.getTotalDelivered());
        System.out.println("Status: " + summary.getStatus());
    }
}
