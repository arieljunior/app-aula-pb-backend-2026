package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private final String customerName;
    private final String contact;
    private final List<OrderItem> items = new ArrayList<>();
    private PaymentMethod paymentMethod;
    private double amountPaid;
    private OrderStatus status;

    public Order(String customerName, String contact) {
        this.customerName = customerName;
        this.contact = contact;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public void registerPayment(PaymentMethod paymentMethod, double amountPaid) {
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
    }

    public double calculateChange() {
        if (paymentMethod != PaymentMethod.CASH) {
            return 0;
        }
        return amountPaid - calculateTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContact() {
        return contact;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
