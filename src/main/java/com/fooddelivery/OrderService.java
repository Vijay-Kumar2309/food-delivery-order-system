package com.fooddelivery;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderValidator validator;
    private final List<Order> processedOrders = new ArrayList<>();

    public OrderService(OrderValidator validator) {
        this.validator = validator;
    }

    // Process order: validate → confirm → ready for dispatch
    public String processOrder(Order order) {
        List<String> errors = validator.validate(order);

        if (!errors.isEmpty()) {
            order.setStatus("CANCELLED");
            return "Order REJECTED: " + String.join(", ", errors);
        }

        order.setStatus("CONFIRMED");
        processedOrders.add(order);
        return "Order CONFIRMED: " + order.getOrderId();
    }

    // Dispatch a confirmed order
    public String dispatchOrder(Order order) {
        if (!"CONFIRMED".equals(order.getStatus())) {
            return "Cannot dispatch. Order status is: " + order.getStatus();
        }
        order.setStatus("DISPATCHED");
        return "Order DISPATCHED: " + order.getOrderId();
    }

    // Mark order as delivered
    public String deliverOrder(Order order) {
        if (!"DISPATCHED".equals(order.getStatus())) {
            return "Cannot deliver. Order status is: " + order.getStatus();
        }
        order.setStatus("DELIVERED");
        return "Order DELIVERED: " + order.getOrderId();
    }

    // Cancel an order (only if PENDING or CONFIRMED)
    public String cancelOrder(Order order) {
        if ("DISPATCHED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            return "Cannot cancel. Order already " + order.getStatus();
        }
        order.setStatus("CANCELLED");
        return "Order CANCELLED: " + order.getOrderId();
    }

    public List<Order> getProcessedOrders() {
        return processedOrders;
    }

    public double calculateTotalRevenue() {
        return processedOrders.stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }
}
