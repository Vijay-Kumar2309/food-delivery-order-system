package com.fooddelivery;

import java.util.List;

public class Order {

    private String orderId;
    private String customerName;
    private String deliveryAddress;
    private List<OrderItem> items;
    private String status;   // PENDING, CONFIRMED, DISPATCHED, DELIVERED, CANCELLED

    public Order(String orderId, String customerName, String deliveryAddress, List<OrderItem> items) {
        this.orderId         = orderId;
        this.customerName    = customerName;
        this.deliveryAddress = deliveryAddress;
        this.items           = items;
        this.status          = "PENDING";
    }

    public String getOrderId()          { return orderId; }
    public String getCustomerName()     { return customerName; }
    public String getDeliveryAddress()  { return deliveryAddress; }
    public List<OrderItem> getItems()   { return items; }
    public String getStatus()           { return status; }

    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== Order: ").append(orderId).append(" ==========\n");
        sb.append("Customer        : ").append(customerName).append("\n");
        sb.append("Delivery To     : ").append(deliveryAddress).append("\n");
        sb.append("Status          : ").append(status).append("\n");
        sb.append("Items:\n");
        if (items != null) items.forEach(i -> sb.append(i).append("\n"));
        sb.append(String.format("Total Amount    : $%.2f%n", getTotalAmount()));
        sb.append("==========================================");
        return sb.toString();
    }
}
