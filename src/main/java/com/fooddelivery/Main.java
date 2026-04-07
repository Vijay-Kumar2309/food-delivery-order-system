package com.fooddelivery;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        OrderValidator validator = new OrderValidator();
        OrderService orderService = new OrderService(validator);

        // ── Valid Order ──────────────────────────────────────────────────────
        Order order1 = new Order(
            "ORD-001", "Ajay Kumar", "123 Anna Salai, Chennai",
            List.of(
                new OrderItem("Chicken Burger", 2, 8.99),
                new OrderItem("Fries",          1, 3.49),
                new OrderItem("Coke",           2, 1.99)
            )
        );

        // ── Another Valid Order ──────────────────────────────────────────────
        Order order2 = new Order(
            "ORD-002", "Priya Sharma", "45 Mount Road, Chennai",
            List.of(
                new OrderItem("Veg Pizza",   1, 12.99),
                new OrderItem("Garlic Bread", 1, 4.49)
            )
        );

        // ── Invalid Order (no items, no address) ────────────────────────────
        Order order3 = new Order(
            "ORD-003", "", "",
            List.of()
        );

        // ── Process all orders ───────────────────────────────────────────────
        System.out.println("\n===== FOOD DELIVERY ORDER SYSTEM =====\n");

        System.out.println(orderService.processOrder(order1));
        System.out.println(order1);

        System.out.println(orderService.processOrder(order2));
        System.out.println(order2);

        System.out.println(orderService.processOrder(order3));  // should be rejected

        // ── Dispatch & Deliver order1 ────────────────────────────────────────
        System.out.println("\n" + orderService.dispatchOrder(order1));
        System.out.println(orderService.deliverOrder(order1));

        // ── Revenue summary ──────────────────────────────────────────────────
        System.out.printf("%nTotal Revenue from Processed Orders: $%.2f%n",
                orderService.calculateTotalRevenue());
    }
}
