package com.fooddelivery;

public class OrderItem {

    private String itemName;
    private int quantity;
    private double price;

    public OrderItem(String itemName, int quantity, double price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price    = price;
    }

    public String getItemName() { return itemName; }
    public int getQuantity()    { return quantity; }
    public double getPrice()    { return price; }

    public double getTotalPrice() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("  - %s x%d @ $%.2f = $%.2f", itemName, quantity, price, getTotalPrice());
    }
}
