package org.example.model;

public enum PaymentMethod {
    CASH("Dinheiro"),
    PIX("PIX"),
    CARD("Cartão");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
