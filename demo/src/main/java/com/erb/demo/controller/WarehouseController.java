package com.erb.demo.controller;

import com.erb.demo.Projection.WarehouseBasicView;
import com.erb.demo.model.Warehouse;
import com.erb.demo.service.WarehouseService;
import com.erb.demo.Projection.WarehouseAnalyticsView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService service;

    @GetMapping
    public List<Warehouse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody @Valid Warehouse warehouse) {
        Warehouse saved = service.save(warehouse);
        return ResponseEntity.ok(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> update(@PathVariable Long id, @RequestBody @Valid Warehouse updated) {
        Warehouse existing = service.getById(id);
        if (existing != null) {
            existing.setLocation(updated.getLocation());
            existing.setCapacity(updated.getCapacity());
            Warehouse saved = service.save(existing);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public Page<WarehouseBasicView> getWarehouseSummary(Pageable pageable) {
        return service.getSummaryRaw(pageable);
    }

    @GetMapping("/advanced-search")
    public Page<WarehouseBasicView> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String countryCode,
            Pageable pageable) {
        return service.advancedSearch(name, location, countryCode, pageable);
    }

    @GetMapping("/analytics")
    public WarehouseAnalyticsView getAnalytics() {
        return service.getWarehouseAnalytics(null);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToCsv() {
        byte[] csvBytes = service.exportWarehousesToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=warehouses.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvBytes);
    }

    @GetMapping("/filter-by-date")
    public List<Warehouse> filterByDate(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
    ) {
        return service.filterByDateRange(start, end);
    }
}
