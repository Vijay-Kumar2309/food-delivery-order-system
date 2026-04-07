package com.fooddelivery;

import org.junit.jupiter.api.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderValidator Tests")
class OrderValidatorTest {

    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }

    // ── Valid Order Tests ────────────────────────────────────────────────────

    @Test
    @DisplayName("Valid order should pass validation")
    void testValidOrder() {
        Order order = new Order(
            "ORD-001", "Ajay Kumar", "123 Anna Salai, Chennai",
            List.of(new OrderItem("Burger", 2, 8.99))
        );
        assertTrue(validator.isValid(order), "Order with all valid fields should pass");
    }

    @Test
    @DisplayName("Valid order should return no errors")
    void testValidOrder_NoErrors() {
        Order order = new Order(
            "ORD-002", "Priya", "45 Mount Road",
            List.of(new OrderItem("Pizza", 1, 12.99))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.isEmpty(), "No errors expected for valid order");
    }

    // ── Invalid Order Scenarios ───────────────────────────────────────────────

    @Test
    @DisplayName("Null order should return error")
    void testNullOrder() {
        List<String> errors = validator.validate(null);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("null"));
    }

    @Test
    @DisplayName("Empty customer name should fail validation")
    void testEmptyCustomerName() {
        Order order = new Order(
            "ORD-003", "", "123 Street",
            List.of(new OrderItem("Burger", 1, 8.99))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Customer name")));
    }

    @Test
    @DisplayName("Null customer name should fail validation")
    void testNullCustomerName() {
        Order order = new Order(
            "ORD-004", null, "123 Street",
            List.of(new OrderItem("Burger", 1, 8.99))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Customer name")));
    }

    @Test
    @DisplayName("Empty delivery address should fail validation")
    void testEmptyDeliveryAddress() {
        Order order = new Order(
            "ORD-005", "Ajay", "",
            List.of(new OrderItem("Burger", 1, 8.99))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Delivery address")));
    }

    @Test
    @DisplayName("Empty items list should fail validation")
    void testEmptyItemsList() {
        Order order = new Order(
            "ORD-006", "Ajay", "123 Street",
            Collections.emptyList()
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("at least one item")));
    }

    @Test
    @DisplayName("Null items list should fail validation")
    void testNullItemsList() {
        Order order = new Order(
            "ORD-007", "Ajay", "123 Street", null
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("at least one item")));
    }

    @Test
    @DisplayName("Item with zero quantity should fail validation")
    void testItemWithZeroQuantity() {
        Order order = new Order(
            "ORD-008", "Ajay", "123 Street",
            List.of(new OrderItem("Burger", 0, 8.99))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("quantity")));
    }

    @Test
    @DisplayName("Item with negative price should fail validation")
    void testItemWithNegativePrice() {
        Order order = new Order(
            "ORD-009", "Ajay", "123 Street",
            List.of(new OrderItem("Burger", 1, -5.00))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("price")));
    }

    @Test
    @DisplayName("Order below minimum amount should fail validation")
    void testOrderBelowMinimumAmount() {
        Order order = new Order(
            "ORD-010", "Ajay", "123 Street",
            List.of(new OrderItem("Candy", 1, 1.00))
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Minimum order amount")));
    }

    @Test
    @DisplayName("Order with multiple validation errors should report all of them")
    void testMultipleErrors() {
        Order order = new Order(
            "ORD-011", "", "",
            Collections.emptyList()
        );
        List<String> errors = validator.validate(order);
        assertTrue(errors.size() >= 3, "Should report at least 3 errors");
    }
}
