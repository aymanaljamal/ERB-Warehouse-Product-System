package com.erb.demo.repository;

import com.erb.demo.Projection.WarehouseBasicView;
import com.erb.demo.Projection.CountryWarehouseSummary;
import com.erb.demo.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    long countByCreatedDateAfter(LocalDateTime date);
    List<Warehouse> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query(
            value = """
                SELECT 
                    w.name AS name,
                    w.country_code AS countryCode,
                    w.created_date AS createdDate,
                    COUNT(e.id) AS employeeCount,
                    CASE 
                        WHEN DATEDIFF(CURRENT_DATE, w.created_date) > 7 THEN 'old'
                        ELSE 'new'
                    END AS status
                FROM warehouse w
                LEFT JOIN employee e ON e.warehouse_id = w.id
                GROUP BY w.id, w.name, w.country_code, w.created_date
                """,
            countQuery = "SELECT COUNT(DISTINCT w.id) FROM warehouse w",
            nativeQuery = true
    )
    Page<WarehouseBasicView> findWarehouseBasicFields(Pageable pageable);

    @Query(
            value = """
                SELECT 
                    w.name AS name,
                    w.country_code AS countryCode,
                    w.created_date AS createdDate,
                    COUNT(e.id) AS employeeCount,
                    CASE 
                        WHEN DATEDIFF(CURRENT_DATE, w.created_date) > 7 THEN 'old'
                        ELSE 'new'
                    END AS status
                FROM warehouse w
                LEFT JOIN employee e ON e.warehouse_id = w.id
                WHERE w.name LIKE %:name%
                GROUP BY w.id, w.name, w.country_code, w.created_date
                """,
            countQuery = "SELECT COUNT(DISTINCT w.id) FROM warehouse w WHERE w.name LIKE %:name%",
            nativeQuery = true
    )
    Page<WarehouseBasicView> searchByName(@Param("name") String name, Pageable pageable);

    @Query(
            value = """
                SELECT 
                    w.name AS name,
                    w.country_code AS countryCode,
                    w.created_date AS createdDate
                FROM warehouse w
                WHERE w.name ILIKE %:name%
                """,
            countQuery = "SELECT COUNT(*) FROM warehouse w WHERE w.name ILIKE %:name%",
            nativeQuery = true
    )
    Page<WarehouseBasicView> searchByNameNative(@Param("name") String name, Pageable pageable);

    @Query(
            value = """
                SELECT 
                    w.country_code AS countryCode,
                    COUNT(*) AS warehouseCount
                FROM warehouse w
                GROUP BY w.country_code
                """,
            nativeQuery = true
    )
    List<CountryWarehouseSummary> getWarehouseCountPerCountry();
}
