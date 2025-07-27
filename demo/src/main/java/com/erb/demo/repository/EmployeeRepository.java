package com.erb.demo.repository;

import com.erb.demo.dto.DeliveryEmployeeSummary;
import com.erb.demo.model.Employee;
import com.erb.demo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    @Query("""
    SELECT DISTINCT e.id
    FROM Employee e
    LEFT JOIN Delivery d ON d.deliveredBy.id = e.id
    LEFT JOIN Order o ON o.id = d.order.id
    WHERE e.rank = 'DELIVERY'
      AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:status IS NULL OR o.status = :status OR o.status IS NULL)
""")
    Page<Long> findDeliveryEmployeeIds(@Param("name") String name,
                                       @Param("status") Order.OrderStatus status,
                                       Pageable pageable);


}
