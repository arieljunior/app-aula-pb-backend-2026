package org.example.ui;

import org.example.model.Flavor;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentMethod;
import org.example.model.Size;
import org.example.repository.PizzaMenu;
import org.example.service.OrderService;
import org.example.util.Money;

import java.util.Scanner;

public class TerminalMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final OrderService orderService;
    private final PizzaMenu pizzaMenu;

    public TerminalMenu(OrderService orderService, PizzaMenu pizzaMenu) {
        this.orderService = orderService;
        this.pizzaMenu = pizzaMenu;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== PIZZARIA DO SEU ANTONIO =====");
            System.out.println("1 - Novo Pedido");
            System.out.println("0 - Sair");
            int option = readInt("Escolha uma opção: ");
            if (option == 1) {
                newOrder();
            } else if (option == 0) {
                running = false;
                System.out.println("Até logo!");
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void newOrder() {
        System.out.println();
        System.out.println("--- Novo Pedido ---");
        String customerName = readRequiredText("Nome do cliente: ");
        String contact = readRequiredText("Endereço ou telefone de contato: ");
        Order order = orderService.startOrder(customerName, contact);

        boolean addMore = true;
        while (addMore) {
            addPizza(order);
            addMore = readOption("Deseja adicionar outra pizza? (1 - Sim, 2 - Não): ", 1, 2) == 1;
        }

        System.out.println();
        System.out.println("Valor total do pedido: " + Money.format(order.calculateTotal()));
        choosePayment(order);

        printSummary(order);
        int confirmation = readOption("1 - Confirmar pedido, 2 - Cancelar pedido: ", 1, 2);
        if (confirmation == 1) {
            int orderId = orderService.finalizeOrder(order);
            System.out.println("Pedido #" + orderId + " enviado para a cozinha!");
        } else {
            System.out.println("Pedido cancelado. Nada foi enviado para a cozinha.");
        }
    }

    private void addPizza(Order order) {
        printPizzaMenu();
        Size size = readSize();
        int mode = readOption("Modalidade (1 - Inteira, 2 - Meio a meio): ", 1, 2);

        int code1;
        int code2 = 0;
        if (mode == 2) {
            code1 = readFlavorCode("Código do primeiro sabor: ");
            code2 = readSecondFlavorCode(code1);
        } else {
            code1 = readFlavorCode("Código do sabor: ");
        }

        System.out.print("Observação de preparo (Enter para pular): ");
        String notes = scanner.nextLine().trim();

        if (mode == 2) {
            orderService.addHalfAndHalfPizza(order, size, code1, code2, notes);
        } else {
            orderService.addWholePizza(order, size, code1, notes);
        }
        System.out.println("Pizza adicionada ao pedido.");
    }

    private void printPizzaMenu() {
        System.out.println();
        System.out.println("--- Cardápio ---");
        System.out.printf("%-4s %-22s %-12s %-12s %-12s%n", "Cód", "Sabor", "Pequena", "Média", "Grande");
        for (Flavor flavor : pizzaMenu.listFlavors()) {
            System.out.printf("%-4d %-22s %-12s %-12s %-12s%n",
                    flavor.getCode(),
                    flavor.getName(),
                    Money.format(flavor.getPrice(Size.SMALL)),
                    Money.format(flavor.getPrice(Size.MEDIUM)),
                    Money.format(flavor.getPrice(Size.LARGE)));
        }
        System.out.println();
    }

    private Size readSize() {
        int option = readOption("Tamanho (1 - Pequena, 2 - Média, 3 - Grande): ", 1, 3);
        return Size.values()[option - 1];
    }

    private int readFlavorCode(String message) {
        while (true) {
            int code = readInt(message);
            if (orderService.isValidFlavorCode(code)) {
                return code;
            }
            System.out.println("Código de sabor inválido. Tente novamente.");
        }
    }

    private int readSecondFlavorCode(int firstCode) {
        while (true) {
            int code = readFlavorCode("Código do segundo sabor: ");
            if (code != firstCode) {
                return code;
            }
            System.out.println("Escolha um sabor diferente do primeiro.");
        }
    }

    private void choosePayment(Order order) {
        int option = readOption("Forma de pagamento (1 - Dinheiro, 2 - PIX, 3 - Cartão): ", 1, 3);
        if (option == 1) {
            payWithCash(order);
        } else {
            PaymentMethod method = option == 2 ? PaymentMethod.PIX : PaymentMethod.CARD;
            orderService.processElectronicPayment(order, method);
            System.out.println("Pagamento via " + method.getLabel() + " aprovado.");
        }
    }

    private void payWithCash(Order order) {
        boolean paid = false;
        while (!paid) {
            double amount = readMoney("Troco para quanto? R$ ");
            paid = orderService.processCashPayment(order, amount);
            if (!paid) {
                System.out.println("Valor insuficiente. Informe uma quantia igual ou superior ao total de "
                        + Money.format(order.calculateTotal()) + ".");
            }
        }
    }

    private void printSummary(Order order) {
        System.out.println();
        System.out.println("========== RESUMO DA COMANDA ==========");
        System.out.println("Cliente: " + order.getCustomerName());
        System.out.println("Contato: " + order.getContact());
        System.out.println("Pizzas:");
        int number = 1;
        for (OrderItem item : order.getItems()) {
            System.out.println("  " + number + ". " + item.getDescription() + " - " + Money.format(item.getPrice()));
            if (!item.getNotes().isEmpty()) {
                System.out.println("     Obs: " + item.getNotes());
            }
            number++;
        }
        System.out.println("Valor total: " + Money.format(order.calculateTotal()));
        System.out.println("Forma de pagamento: " + order.getPaymentMethod().getLabel());
        System.out.println("Valor pago: " + Money.format(order.getAmountPaid()));
        System.out.println("Troco a levar: " + Money.format(order.calculateChange()));
        System.out.println("=======================================");
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números.");
            }
        }
    }

    private int readOption(String message, int min, int max) {
        while (true) {
            int option = readInt(message);
            if (option >= min && option <= max) {
                return option;
            }
            System.out.println("Opção inválida. Escolha um número entre " + min + " e " + max + ".");
        }
    }

    private double readMoney(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número, por exemplo 100,00.");
            }
        }
    }

    private String readRequiredText(String message) {
        while (true) {
            System.out.print(message);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Este campo é obrigatório.");
        }
    }
}
