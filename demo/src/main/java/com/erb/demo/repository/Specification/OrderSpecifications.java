package com.erb.demo.repository.Specification;
import com.erb.demo.dto.DTO.OrderSearchCriteria;
import com.erb.demo.model.*;
import com.erb.demo.model.Order;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
public class OrderSpecifications {

    public static Specification<Order> build(OrderSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join مع Customer
            Join<Order, Customer> customerJoin = root.join("customer", JoinType.INNER);
            // Join مع Employee
            Join<Order, Employee> employeeJoin = root.join("employee", JoinType.LEFT);


            if (criteria.getCustomerName() != null) {
                predicates.add(cb.like(cb.lower(customerJoin.get("name")),
                        "%" + criteria.getCustomerName().toLowerCase() + "%"));
            }

            if (criteria.getEmployeeName() != null) {
                predicates.add(cb.like(cb.lower(employeeJoin.get("name")),
                        "%" + criteria.getEmployeeName().toLowerCase() + "%"));
            }

            if (criteria.getOrderStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getOrderStatus()));
            }

            if (criteria.getDeliveredAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deliveredAt"), criteria.getDeliveredAfter()));
            }

            if (criteria.getDeliveredBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deliveredAt"), criteria.getDeliveredBefore()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
