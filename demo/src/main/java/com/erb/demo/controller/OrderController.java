package com.erb.demo.controller;

import com.erb.demo.dto.DTO.CreateOrderRequest;
import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.DTO.ProductStockDto;
import com.erb.demo.dto.DTO.UpdateOrderStatusRequest;
import com.erb.demo.model.Order;
import com.erb.demo.service.OrderService;
import com.erb.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;
    @Autowired
    private  ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = service.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public String getAll() {
        return service.getAll().toString();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF', 'CUSTOMER')")
    public Order getById(@PathVariable Long id, Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Order order = service.getById(id);
            String email = authentication.getName();
            if (order == null || !order.getCustomer().getEmail().equals(email)) {
                throw new AccessDeniedException("You can only access your own orders.");
            }
            return order;
        }
        return service.getById(id);
    }

/*
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF', 'CUSTOMER')")
    public Order create(@RequestBody @Valid Order order) {
        order.setCreatedAt(LocalDateTime.now());
        return service.save(order);
    }
*/

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF')")
    public Order update(@PathVariable Long id, @RequestBody @Valid Order updated) {
        Order existing = service.getById(id);
        if (existing != null) {
            existing.setStatus(updated.getStatus());
            existing.setDeliveredAt(updated.getDeliveredAt());
            return service.save(existing);
        }
        return null;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<Page<Order>> getAllOrders(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<Order> ordersPage = service.getAllOrders(pageable);
        return ResponseEntity.ok(ordersPage);
    }

    //========================================================================

    @GetMapping("/products")
    public ResponseEntity<List<ProductStockDto>> listAvailableProducts() {
        List<ProductStockDto> products = productService.getAvailableProductsWithStock();
        return ResponseEntity.ok(products);
    }
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        OrderDto createdOrder = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    @PostMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest statusRequest) {
        service.updateOrderStatus(orderId, statusRequest.getStatus());
        return ResponseEntity.ok("Order status updated");
    }

}
