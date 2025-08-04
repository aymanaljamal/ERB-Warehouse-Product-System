package com.erb.demo.model;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDelivery() {
        Delivery delivery = Delivery.builder()
                .deliveredAt(LocalDateTime.now().minusDays(1))  // valid past date
                .order(new Order())                             // dummy order object
                .deliveredBy(new Employee())                    // dummy employee object
                .build();

        Set<ConstraintViolation<Delivery>> violations = validator.validate(delivery);

        System.out.println("Violations: " + violations.size());
        assertThat(violations).isEmpty();
    }

    @Test
    public  void testDeliveryWithFutureDate() {
        Delivery delivery = Delivery.builder()
                .deliveredAt(LocalDateTime.now().plusDays(1)) // invalid: future date
                .order(new Order())
                .deliveredBy(new Employee())
                .build();

        Set<ConstraintViolation<Delivery>> violations = validator.validate(delivery);

        violations.forEach(v -> System.out.println(v.getMessage()));
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("cannot be in the future"))).isTrue();
    }

    @Test
    public  void testDeliveryMissingOrder() {
        Delivery delivery = Delivery.builder()
                .deliveredAt(LocalDateTime.now())
                .deliveredBy(new Employee())
                .build();

        Set<ConstraintViolation<Delivery>> violations = validator.validate(delivery);

        violations.forEach(v -> System.out.println(v.getMessage()));
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Order must be provided"))).isTrue();
    }

    @Test
    public void testDeliveryMissingEmployee() {
        Delivery delivery = Delivery.builder()
                .deliveredAt(LocalDateTime.now())
                .order(new Order())
                .build();

        Set<ConstraintViolation<Delivery>> violations = validator.validate(delivery);

        violations.forEach(v -> System.out.println(v.getMessage()));
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("DeliveredBy (employee) is required"))).isTrue();
    }
}
