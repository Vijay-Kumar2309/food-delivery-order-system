package com.fooddelivery;

import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderService Tests")
class OrderServiceTest {

    private OrderService orderService;
    private Order validOrder;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(new OrderValidator());
        validOrder = new Order(
            "ORD-001", "Ajay Kumar", "123 Anna Salai, Chennai",
            List.of(
                new OrderItem("Chicken Burger", 2, 8.99),
                new OrderItem("Coke",           1, 1.99)
            )
        );
    }

    // ── Order Processing Tests ────────────────────────────────────────────────

    @Test
    @DisplayName("Valid order should be CONFIRMED after processing")
    void testValidOrder_IsConfirmed() {
        orderService.processOrder(validOrder);
        assertEquals("CONFIRMED", validOrder.getStatus());
    }

    @Test
    @DisplayName("Invalid order should be CANCELLED after processing")
    void testInvalidOrder_IsCancelled() {
        Order badOrder = new Order("ORD-BAD", "", "", List.of());
        orderService.processOrder(badOrder);
        assertEquals("CANCELLED", badOrder.getStatus());
    }

    @Test
    @DisplayName("processOrder should return CONFIRMED message for valid order")
    void testProcessOrder_ConfirmedMessage() {
        String result = orderService.processOrder(validOrder);
        assertTrue(result.contains("CONFIRMED"));
    }

    @Test
    @DisplayName("processOrder should return REJECTED message for invalid order")
    void testProcessOrder_RejectedMessage() {
        Order badOrder = new Order("ORD-BAD", "", "", List.of());
        String result = orderService.processOrder(badOrder);
        assertTrue(result.contains("REJECTED"));
    }

    // ── Order Total Tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Order total should be sum of all item totals")
    void testOrderTotal_IsAccurate() {
        // (8.99 * 2) + (1.99 * 1) = 19.97
        assertEquals(19.97, validOrder.getTotalAmount(), 0.001);
    }

    // ── Dispatch Tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("CONFIRMED order should move to DISPATCHED")
    void testDispatch_FromConfirmed() {
        orderService.processOrder(validOrder);
        orderService.dispatchOrder(validOrder);
        assertEquals("DISPATCHED", validOrder.getStatus());
    }

    @Test
    @DisplayName("PENDING order cannot be dispatched")
    void testDispatch_FromPending_ShouldFail() {
        String result = orderService.dispatchOrder(validOrder); // still PENDING
        assertTrue(result.contains("Cannot dispatch"));
    }

    // ── Delivery Tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("DISPATCHED order should move to DELIVERED")
    void testDeliver_FromDispatched() {
        orderService.processOrder(validOrder);
        orderService.dispatchOrder(validOrder);
        orderService.deliverOrder(validOrder);
        assertEquals("DELIVERED", validOrder.getStatus());
    }

    @Test
    @DisplayName("CONFIRMED order cannot skip to DELIVERED")
    void testDeliver_FromConfirmed_ShouldFail() {
        orderService.processOrder(validOrder);
        String result = orderService.deliverOrder(validOrder); // not dispatched yet
        assertTrue(result.contains("Cannot deliver"));
    }

    // ── Cancel Tests ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("CONFIRMED order can be cancelled")
    void testCancel_ConfirmedOrder() {
        orderService.processOrder(validOrder);
        orderService.cancelOrder(validOrder);
        assertEquals("CANCELLED", validOrder.getStatus());
    }

    @Test
    @DisplayName("DISPATCHED order cannot be cancelled")
    void testCancel_DispatchedOrder_ShouldFail() {
        orderService.processOrder(validOrder);
        orderService.dispatchOrder(validOrder);
        String result = orderService.cancelOrder(validOrder);
        assertTrue(result.contains("Cannot cancel"));
    }

    // ── Revenue Tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Revenue should accumulate from confirmed orders")
    void testRevenue_FromMultipleOrders() {
        Order order2 = new Order(
            "ORD-002", "Priya", "45 Mount Road",
            List.of(new OrderItem("Pizza", 1, 12.99))
        );
        orderService.processOrder(validOrder);
        orderService.processOrder(order2);

        double revenue = orderService.calculateTotalRevenue();
        assertTrue(revenue > 0);
    }

    @Test
    @DisplayName("Cancelled order should not count towards revenue")
    void testRevenue_CancelledOrder_NotCounted() {
        orderService.processOrder(validOrder);
        double beforeCancel = orderService.calculateTotalRevenue();
        orderService.cancelOrder(validOrder);
        double afterCancel = orderService.calculateTotalRevenue();
        assertTrue(afterCancel < beforeCancel);
    }
}
