package com.erb.demo.controller;
import com.erb.demo.dto.DTO.ProductOrderItemsDto;
import com.erb.demo.dto.StockReceiptDto;
import com.erb.demo.model.StockReceipt;
import com.erb.demo.service.StockReceiptService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/stock-receipts")
public class StockReceiptController {

    @Autowired
    private StockReceiptService service;

    @GetMapping("/grouped-order-items")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public ResponseEntity<List<ProductOrderItemsDto>> getGroupedOrderItemsByProduct() {
        return ResponseEntity.ok(service.getAllProductOrderItems());
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public List<StockReceipt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public StockReceipt getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public StockReceipt create(@RequestBody @Valid StockReceipt receipt) {
        receipt.setReceivedAt(LocalDateTime.now());
        return service.save(receipt);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public StockReceipt update(@PathVariable Long id, @RequestBody @Valid StockReceipt updated) {
        StockReceipt existing = service.getById(id);
        if (existing != null) {
            existing.setQuantity(updated.getQuantity());
            existing.setOrderItem(updated.getOrderItem());
            existing.setWarehouse(updated.getWarehouse());
            existing.setEmployee(updated.getEmployee());
            existing.setReceivedAt(updated.getReceivedAt());
            return service.save(existing);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}