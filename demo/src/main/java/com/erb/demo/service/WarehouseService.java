package com.erb.demo.service;

import com.erb.demo.Projection.WarehouseBasicView;
import com.erb.demo.Projection.WarehouseAnalyticsView;
import com.erb.demo.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WarehouseService {

    Page<WarehouseBasicView> getSummaryRaw(Pageable pageable);

    List<Warehouse> getAll();

    Warehouse getById(Long id);

    Warehouse save(Warehouse warehouse);

    void delete(Long id);

    Page<WarehouseBasicView> searchByName(String name, Pageable pageable);

    Page<WarehouseBasicView> advancedSearch(String name, String location, String countryCode, Pageable pageable);

    WarehouseAnalyticsView getWarehouseAnalytics(Long warehouseId);

    List<Warehouse> filterByDateRange(LocalDate startDate, LocalDate endDate);

    byte[] exportWarehousesToCsv();
}
