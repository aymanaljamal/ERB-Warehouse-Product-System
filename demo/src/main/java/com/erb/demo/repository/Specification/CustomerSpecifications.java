package com.erb.demo.repository.Specification;
import com.erb.demo.dto.CustomerSearchCriteria;
import com.erb.demo.model.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecifications {

    public static Specification<Customer> build(CustomerSearchCriteria criteria) {
       Specification <Customer> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (criteria.getEmail() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
            }
            if (criteria.getPhone() != null) {
                predicates.add(cb.like(root.get("phone"), "%" + criteria.getPhone() + "%"));
            }
            if (criteria.getAddress() != null) {
                predicates.add(cb.like(cb.lower(root.get("address")), "%" + criteria.getAddress().toLowerCase() + "%"));
            }
            if (criteria.getMinPoints() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("loyaltyPoints"), criteria.getMinPoints()));
            }
            if (criteria.getMaxPoints() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("loyaltyPoints"), criteria.getMaxPoints()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return specification;
    }
}
