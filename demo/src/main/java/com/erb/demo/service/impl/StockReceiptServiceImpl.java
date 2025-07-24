package com.erb.demo.service.impl;

import com.erb.demo.dto.StockReceiptDto;
import com.erb.demo.model.StockReceipt;
import com.erb.demo.repository.StockReceiptRepository;
import com.erb.demo.service.StockReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockReceiptServiceImpl implements StockReceiptService {

    @Autowired
    private StockReceiptRepository repository;

    @Override
    @Cacheable(value = "stockReceipts")
    public List<StockReceipt> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "stockReceipts", key = "#id")
    public StockReceipt getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "stockReceipts", key = "#receipt.id")
    public StockReceipt save(StockReceipt receipt) {
        return repository.save(receipt);
    }

    @Override
    @CacheEvict(value = "stockReceipts", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
    @Override
    public StockReceiptDto mapToDto(StockReceipt receipt) {
        return StockReceiptDto.builder()
                .id(receipt.getId())
                .quantity(receipt.getQuantity())
                .receivedAt(receipt.getReceivedAt())
                .employeeId(receipt.getEmployee() != null ? receipt.getEmployee().getId() : null)
                .employeeName(receipt.getEmployee() != null ? receipt.getEmployee().getName() : null)
                .productId(receipt.getProduct() != null ? receipt.getProduct().getId() : null)
                .productName(receipt.getProduct() != null ? receipt.getProduct().getName() : null)
                .warehouseId(receipt.getWarehouse() != null ? receipt.getWarehouse().getId() : null)
                .warehouseLocation(receipt.getWarehouse() != null ? receipt.getWarehouse().getLocation() : null)
                .build();
    }
    @Override
    public List<StockReceiptDto> mapToDtoList(List<StockReceipt> receipts) {
        return receipts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
