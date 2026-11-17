package org.example.model;

public enum Size {
    SMALL("Pequena"),
    MEDIUM("Média"),
    LARGE("Grande");

    private final String label;

    Size(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
