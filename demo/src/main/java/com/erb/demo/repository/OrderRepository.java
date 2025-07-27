package com.erb.demo.repository;

import com.erb.demo.dto.DTO.OrderDto;
import com.erb.demo.dto.OrderProductDto;
import com.erb.demo.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT oi.product
        FROM OrderItem oi
        WHERE oi.order.id = :orderId
    """)
    List<OrderProductDto> findProductsByOrderId(@Param("orderId") Long orderId);
    @Query("""
   SELECT new com.erb.demo.dto.DTO.OrderDto(
       o.id, o.status, o.createdAt, o.deliveredAt, o.customer.id)
   FROM Order o
   JOIN Delivery d ON d.order.id = o.id
   WHERE d.deliveredBy.id = :employeeId
   AND (:status IS NULL OR o.status = :status)
""")
    List<OrderDto> findOrderDtosByDeliveredByIdAndOptionalStatus(
            @Param("employeeId") Long employeeId,
            @Param("status") Order.OrderStatus status);

}
