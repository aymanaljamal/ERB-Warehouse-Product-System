package com.erb.demo.service.impl;
import com.erb.demo.EmailService.EmailService;

import com.erb.demo.Plugin.CustomerPlugin.CustomerPluginExecutor;
import com.erb.demo.Plugin.OrdersPlugins.OrderPluginExecutor;
import com.erb.demo.Projection.OrderSummaryDto;
import com.erb.demo.dto.DTO.CreateOrderRequest;
import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.DTO.OrderItemDto;
import com.erb.demo.dto.DTO.OrderItemRequest;
import com.erb.demo.dto.OrderDetailsDto;
import com.erb.demo.model.*;
import com.erb.demo.repository.CustomerRepository;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.repository.ProductRepository;
import com.erb.demo.repository.WarehouseProductRepository;
import com.erb.demo.service.OrderService;
import com.erb.demo.state.OrderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  WarehouseProductRepository warehouseProductRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderPluginExecutor pluginExecutor;
    @Autowired
    private CustomerPluginExecutor customerPluginExecutor;


    @Override
    @Cacheable(value = "orders")
    public List<Order> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Order getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "orders", key = "#order.id")
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return repository.findAll(pageable);
    }
    @Override
    public List<OrderDto> getAllOrders() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .status(String.valueOf(order.getStatus()))
                .createdAt(order.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .items(order.getItems() != null ? order.getItems().stream()
                        .map(item -> OrderItemDto.builder()
                                .id(item.getId())
                                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                                .quantity(item.getQuantity())
                                .build())
                        .toList() : List.of())
                .build();
    }
    //==================================================


    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        for (OrderItemRequest itemReq : request.getItems()) {
            int totalAvailable = warehouseProductRepository.getTotalQuantityByProductId(itemReq.getProductId());
            if (totalAvailable < itemReq.getQuantity()) {
                throw new RuntimeException("Not enough stock for product ID " + itemReq.getProductId());
            }
        }
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            deductStock(product.getId(), itemReq.getQuantity());
        }
        order.setItems(orderItems);
        repository.save(order);
        return convertToDto(order);
    }

    private void deductStock(Long productId, int quantity) {
        List<WarehouseProduct> stocks = warehouseProductRepository.findByProductIdAndQuantityGreaterThan(productId, 0);
        int totalAvailable = stocks.stream().mapToInt(WarehouseProduct::getQuantity).sum();

        if (totalAvailable < quantity) {
            throw new RuntimeException("Insufficient stock: required = " + quantity + ", available = " + totalAvailable + " for product " + productId);
        }

        int remaining = quantity;
        for (WarehouseProduct stock : stocks) {
            if (stock.getQuantity() >= remaining) {
                stock.setQuantity(stock.getQuantity() - remaining);
                warehouseProductRepository.save(stock);
                break;
            } else {
                remaining -= stock.getQuantity();
                stock.setQuantity(0);
                warehouseProductRepository.save(stock);
            }
        }
    }

    public void updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderContext context = new OrderContext(order, emailService);
        Order.OrderStatus currentStatus = order.getStatus();

        switch (newStatus) {
            case PROCESSING -> {
                if (currentStatus != Order.OrderStatus.CREATED)
                    throw new IllegalStateException("Only CREATED orders can move to PROCESSING");
                context.next();
            }
            case SHIPPED -> {
                if (currentStatus != Order.OrderStatus.PROCESSING)
                    throw new IllegalStateException("Only PROCESSING orders can move to SHIPPED");
                context.next();
            }
            case DELIVERED -> {
                if (currentStatus != Order.OrderStatus.SHIPPED)
                    throw new IllegalStateException("Only SHIPPED orders can move to DELIVERED");

                context.next();
                order.setDeliveredAt(LocalDateTime.now());
                pluginExecutor.execute(order);
            }
            case CANCELLED -> {
                context.cancel();
            }
            default -> throw new IllegalArgumentException("Invalid transition");
        }
        pluginExecutor.execute(order);

        repository.save(order);
    }

    public OrderDto convertToDto(Order order) {
        List<OrderItemDto> itemsDto = order.getItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus().toString())
                .createdAt(order.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .customerId(order.getCustomer().getId())
                .items(itemsDto)
                .build();
    }
    @Override
    public OrderDetailsDto getOrderWithEmployee(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return new OrderDetailsDto(order);
    }
    @Override
    public List<OrderDetailsDto> getOrdersCreatedByStaff() {
        return repository.findAllOrdersByStaffOnly()
                .stream()
                .map(OrderDetailsDto::new)
                .toList();
    }
    @Override
    public Page<OrderSummaryDto> getOldUndeliveredOrders(LocalDateTime threeDaysAgo, Pageable pageable) {
        return repository.findOldUndeliveredOrdersSummary(threeDaysAgo, pageable);
    }
    @Override
    public Order processOrder(Long orderId) {
        Order order = repository.findById(orderId).orElseThrow();
        pluginExecutor.execute(order);
        return repository.save(order);
    }

}
