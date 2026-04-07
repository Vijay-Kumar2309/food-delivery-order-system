package com.fooddelivery;

import java.util.ArrayList;
import java.util.List;

public class OrderValidator {

    // Returns list of validation errors. Empty list = valid order.
    public List<String> validate(Order order) {
        List<String> errors = new ArrayList<>();

        if (order == null) {
            errors.add("Order cannot be null");
            return errors;
        }

        // Customer name check
        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            errors.add("Customer name is required");
        }

        // Delivery address check
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().trim().isEmpty()) {
            errors.add("Delivery address is required");
        }

        // Items check
        if (order.getItems() == null || order.getItems().isEmpty()) {
            errors.add("Order must have at least one item");
        } else {
            for (OrderItem item : order.getItems()) {
                if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
                    errors.add("Item name cannot be empty");
                }
                if (item.getQuantity() <= 0) {
                    errors.add("Item quantity must be greater than 0: " + item.getItemName());
                }
                if (item.getPrice() <= 0) {
                    errors.add("Item price must be greater than 0: " + item.getItemName());
                }
            }
        }

        // Minimum order amount check
        if (order.getTotalAmount() < 5.0) {
            errors.add("Minimum order amount is $5.00");
        }

        return errors;
    }

    public boolean isValid(Order order) {
        return validate(order).isEmpty();
    }
}
