package org.example.model;

public enum OrderStatus {
    RECEIVED("Recebido"),
    IN_PREPARATION("Em Preparo"),
    OUT_FOR_DELIVERY("Saiu para Entrega");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
