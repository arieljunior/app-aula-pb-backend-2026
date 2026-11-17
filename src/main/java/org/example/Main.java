package org.example;

import org.example.repository.PizzaMenu;
import org.example.service.OrderService;
import org.example.ui.TerminalMenu;

public class Main {
    public static void main(String[] args) {
        PizzaMenu pizzaMenu = new PizzaMenu();
        OrderService orderService = new OrderService(pizzaMenu);
        TerminalMenu terminalMenu = new TerminalMenu(orderService, pizzaMenu);
        terminalMenu.start();
    }
}
