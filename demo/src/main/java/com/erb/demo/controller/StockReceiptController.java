package com.erb.demo.controller;
import com.erb.demo.model.StockReceipt;
import com.erb.demo.service.StockReceiptService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-receipts")
public class StockReceiptController {

    @Autowired
    private StockReceiptService service;

    @GetMapping
    public List<StockReceipt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public StockReceipt getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public StockReceipt create(@RequestBody @Valid StockReceipt receipt) {
        receipt.setReceivedAt(LocalDateTime.now());
        return service.save(receipt);
    }
    @PutMapping("/{id}")
    public StockReceipt update(@PathVariable Long id, @RequestBody @Valid StockReceipt updated) {
        StockReceipt existing = service.getById(id);
        if (existing != null) {
            existing.setQuantity(updated.getQuantity());
            existing.setProduct(updated.getProduct());
            existing.setWarehouse(updated.getWarehouse());
            existing.setEmployee(updated.getEmployee());
            existing.setReceivedAt(updated.getReceivedAt());
            return service.save(existing);
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
