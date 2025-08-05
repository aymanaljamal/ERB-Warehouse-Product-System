package com.erb.demo.service.impl;

import com.erb.demo.EmailService.EmailServiceClient;
import com.erb.demo.Plugin.CustomerPlugin.CustomerPluginExecutor;
import com.erb.demo.Plugin.OrdersPlugins.OrderPluginExecutor;
import com.erb.demo.Projection.OrderSummaryDto;
import com.erb.demo.dto.DTO.*;
import com.erb.demo.dto.OrderDetailsDto;
import com.erb.demo.dto.OrderItemDTO;
import com.erb.demo.dto.OrderResponse;
import com.erb.demo.dto.OrderResponseDTO;
import com.erb.demo.model.*;
import com.erb.demo.repository.CustomerRepository;
import com.erb.demo.repository.OrderRepository;
import com.erb.demo.repository.ProductRepository;
import com.erb.demo.repository.Specification.OrderSpecifications;
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
import org.springframework.web.client.RestTemplate;

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
    private EmailServiceClient emailServiceClient;
    @Autowired
    private OrderPluginExecutor pluginExecutor;
    @Autowired
    private CustomerPluginExecutor customerPluginExecutor;
    @Autowired
    private  CurrencyConversionService currencyService;
    @Autowired
    private RestTemplate restTemplate;
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

        OrderContext context = new OrderContext(order, emailServiceClient);
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

    @Override
    public Page<OrderResponseDTO> getCustomerOrders(Customer customer, Pageable pageable, String baseUrl) {
        Page<Order> orders = repository.findByCustomer(customer, pageable);

        return orders.map(order -> {
            double totalUsd = order.getItems().stream()
                    .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                    .sum();

            double totalIls = currencyService.convertToILS(totalUsd, "USD").block();

            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(i -> new OrderItemDTO(
                            i.getProduct().getName(),
                            i.getQuantity(),
                            i.getProduct().getPrice()
                    )).toList();

            return new OrderResponseDTO(
                    order.getId(),
                    order.getStatus().name(),
                    order.getCreatedAt(),
                    order.getDeliveredAt(),
                    totalIls,
                    baseUrl + "/api/orders/" + order.getId(),
                    itemDTOs
            );
        });
    }
    @Override
    public List<OrderResponse> searchOrders(OrderSearchCriteria criteria) {
        List<Order> orders = repository.findAll(OrderSpecifications.build(criteria));
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .deliveredAt(order.getDeliveredAt())
                        .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                        .employeeName(order.getEmployee() != null ? order.getEmployee().getName() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
