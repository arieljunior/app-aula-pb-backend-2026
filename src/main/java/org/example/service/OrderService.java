package org.example.service;

import org.example.model.Flavor;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.OrderStatus;
import org.example.model.PaymentMethod;
import org.example.model.Size;
import org.example.repository.PizzaMenu;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final PizzaMenu pizzaMenu;
    private final List<Order> activeOrders = new ArrayList<>();
    private int nextOrderId = 1;

    public OrderService(PizzaMenu pizzaMenu) {
        this.pizzaMenu = pizzaMenu;
    }

    public Order startOrder(String customerName, String contact) {
        return new Order(customerName, contact);
    }

    public boolean isValidFlavorCode(int code) {
        return pizzaMenu.findByCode(code) != null;
    }

    public void addWholePizza(Order order, Size size, int code, String notes) {
        Flavor flavor = pizzaMenu.findByCode(code);
        order.addItem(new OrderItem(size, flavor, null, notes));
    }

    public void addHalfAndHalfPizza(Order order, Size size, int code1, int code2, String notes) {
        Flavor flavor1 = pizzaMenu.findByCode(code1);
        Flavor flavor2 = pizzaMenu.findByCode(code2);
        order.addItem(new OrderItem(size, flavor1, flavor2, notes));
    }

    public boolean processCashPayment(Order order, double amountPaid) {
        if (amountPaid < order.calculateTotal()) {
            return false;
        }
        order.registerPayment(PaymentMethod.CASH, amountPaid);
        return true;
    }

    public void processElectronicPayment(Order order, PaymentMethod paymentMethod) {
        order.registerPayment(paymentMethod, order.calculateTotal());
    }

    public int finalizeOrder(Order order) {
        order.setId(nextOrderId++);
        order.setStatus(OrderStatus.RECEIVED);
        activeOrders.add(order);
        return order.getId();
    }
}
