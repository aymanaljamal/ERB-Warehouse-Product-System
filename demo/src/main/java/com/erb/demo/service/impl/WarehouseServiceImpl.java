package com.erb.demo.service.impl;

import com.erb.demo.Projection.WarehouseBasicView;
import com.erb.demo.Projection.WarehouseAnalyticsView;
import com.erb.demo.model.Warehouse;
import com.erb.demo.repository.WarehouseRepository;
import com.erb.demo.service.WarehouseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "warehousesSummary")
    public Page<WarehouseBasicView> getSummaryRaw(Pageable pageable) {
        return repository.findWarehouseBasicFields(pageable);
    }

    @Override
    @Cacheable(value = "warehouses")
    public List<Warehouse> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "warehousesSummary", key = "#name + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<WarehouseBasicView> searchByName(String name, Pageable pageable) {
        return repository.searchByName(name, pageable);
    }

    @Override
    @Cacheable(value = "warehouses", key = "#id")
    public Warehouse getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "warehouses", key = "#warehouse.id")
    public Warehouse save(Warehouse warehouse) {
        return repository.save(warehouse);
    }

    @Override
    @CacheEvict(value = "warehouses", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<WarehouseBasicView> advancedSearch(String name, String location, String countryCode, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("SELECT w.name AS name, w.countryCode AS countryCode, w.createdDate AS createdDate FROM Warehouse w WHERE 1=1");

        if (name != null && !name.isEmpty()) {
            queryStr.append(" AND LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (location != null && !location.isEmpty()) {
            queryStr.append(" AND LOWER(w.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        }
        if (countryCode != null && !countryCode.isEmpty()) {
            queryStr.append(" AND LOWER(w.countryCode) = LOWER(:countryCode)");
        }

        TypedQuery<WarehouseBasicView> query = entityManager.createQuery(queryStr.toString(), WarehouseBasicView.class);

        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (location != null && !location.isEmpty()) {
            query.setParameter("location", location);
        }
        if (countryCode != null && !countryCode.isEmpty()) {
            query.setParameter("countryCode", countryCode);
        }

        // Count query
        String countQueryStr = queryStr.toString().replaceFirst(
                "SELECT w.name AS name, w.countryCode AS countryCode, w.createdDate AS createdDate",
                "SELECT COUNT(w)"
        );
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);

        if (name != null && !name.isEmpty()) {
            countQuery.setParameter("name", name);
        }
        if (location != null && !location.isEmpty()) {
            countQuery.setParameter("location", location);
        }
        if (countryCode != null && !countryCode.isEmpty()) {
            countQuery.setParameter("countryCode", countryCode);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<WarehouseBasicView> resultList = query.getResultList();
        Long count = countQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }

    @Override
    @Cacheable(value = "warehouseAnalytics", key = "'warehouseAnalytics-' + #warehouseId")
    public WarehouseAnalyticsView getWarehouseAnalytics(Long warehouseId) {
        Long total = repository.count();
        Long recent = repository.countByCreatedDateAfter(LocalDate.now().minusMonths(1).atStartOfDay());

        return new WarehouseAnalyticsView(total, recent);
    }

    @Override
    public List<Warehouse> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByCreatedDateBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    @Override
    public byte[] exportWarehousesToCsv() {
        List<Warehouse> warehouses = repository.findAll();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {
            writer.println("ID,Name,Country Code,Created Date");

            for (Warehouse w : warehouses) {
                writer.printf("%d,%s,%s,%s%n",
                        w.getId(),
                        w.getName(),
                        w.getCountryCode(),
                        w.getCreatedDate());
            }

            writer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export CSV", e);
        }
    }
}
