package com.erb.demo.repository;

import com.erb.demo.Projection.EmployeeSummaryProjection;
import com.erb.demo.model.Delivery;
import com.erb.demo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

        @Query(value = """
        SELECT
            e.id AS id,
            e.name AS name,
            e.email AS email,
            w.name AS warehouseName,
            COUNT(d.id) AS totalDelivered,
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM delivery d2
                    WHERE d2.employee_id = e.id
                    AND (
                        SELECT COUNT(*)
                        FROM delivery d3
                        WHERE d3.employee_id = e.id
                        AND d3.delivered_at BETWEEN d2.delivered_at AND d2.delivered_at + INTERVAL 1 HOUR
                    ) >= 10
                ) THEN 'needs_raise'
                ELSE 'normal'
            END AS status
        FROM employee e
        LEFT JOIN warehouse w ON e.warehouse_id = w.id
        LEFT JOIN delivery d ON d.employee_id = e.id
        GROUP BY e.id, e.name, e.email, w.name
        """,
                countQuery = "SELECT COUNT(*) FROM employee",
                nativeQuery = true)
        Page<EmployeeSummaryProjection> getEmployeeSummary(Pageable pageable);


    @Query(value = "SELECT COUNT(*) FROM delivery d WHERE d.employee_id = :employeeId AND d.delivered_at BETWEEN :fromDate AND :toDate", nativeQuery = true)
    int countByDeliveredByAndDeliveredAtBetween(@Param("employeeId") Long employeeId,
                                                @Param("fromDate") Date fromDate,
                                                @Param("toDate") Date toDate);



}
