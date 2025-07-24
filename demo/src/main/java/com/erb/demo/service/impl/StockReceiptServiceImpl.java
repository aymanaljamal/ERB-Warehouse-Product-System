package com.erb.demo.service.impl;

import com.erb.demo.dto.DTO.OrderItemBriefDto;
import com.erb.demo.dto.DTO.ProductOrderItemsDto;
import com.erb.demo.dto.StockReceiptDto;
import com.erb.demo.model.OrderItem;
import com.erb.demo.model.Product;
import com.erb.demo.model.StockReceipt;
import com.erb.demo.repository.OrderItemRepository;
import com.erb.demo.repository.StockReceiptRepository;
import com.erb.demo.service.StockReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockReceiptServiceImpl implements StockReceiptService {

    @Autowired
    private StockReceiptRepository repository;
    @Autowired
    private  OrderItemRepository orderItemRepository;
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
                .orderItemId(receipt.getOrderItem() != null ? receipt.getOrderItem().getId() : null)
                .orderItemName(receipt.getOrderItem() != null && receipt.getOrderItem().getProduct() != null
                        ? receipt.getOrderItem().getProduct().getName() : null)
                .productId(receipt.getOrderItem() != null && receipt.getOrderItem().getProduct() != null
                        ? receipt.getOrderItem().getProduct().getId() : null)
                .productName(receipt.getOrderItem() != null && receipt.getOrderItem().getProduct() != null
                        ? receipt.getOrderItem().getProduct().getName() : null)
                .warehouseId(receipt.getWarehouse() != null ? receipt.getWarehouse().getId() : null)
                .warehouseLocation(receipt.getWarehouse() != null ? receipt.getWarehouse().getLocation() : null)
                .build();
    }
    @Override
    public List<ProductOrderItemsDto> getAllProductOrderItems() {
        List<StockReceipt> receipts =  repository.findAllWhereOrderExists();

        // Group by Product
        Map<Product, List<StockReceipt>> grouped = receipts.stream()
                .filter(sr -> sr.getOrderItem() != null && sr.getOrderItem().getProduct() != null)
                .collect(Collectors.groupingBy(sr -> sr.getOrderItem().getProduct()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    List<OrderItemBriefDto> orderItems = entry.getValue().stream()
                            .map(sr -> {
                                OrderItem orderItem = sr.getOrderItem();
                                return OrderItemBriefDto.builder()
                                        .orderItemId(orderItem.getId())
                                        .orderItemName(product.getName())
                                        .quantity(orderItem.getQuantity())
                                        .orderId(orderItem.getOrder().getId())
                                        .status(orderItem.getOrder().getStatus().toString())
                                        .build();
                            })
                            .toList();

                    return ProductOrderItemsDto.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .orderItems(orderItems)
                            .build();
                })
                .toList();
    }

    @Override
    public List<StockReceiptDto> mapToDtoList(List<StockReceipt> receipts) {
        return receipts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
