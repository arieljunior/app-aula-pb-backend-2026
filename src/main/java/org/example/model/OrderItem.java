package org.example.model;

public class OrderItem {

    private final Size size;
    private final Flavor flavor1;
    private final Flavor flavor2;
    private final String notes;

    public OrderItem(Size size, Flavor flavor1, Flavor flavor2, String notes) {
        this.size = size;
        this.flavor1 = flavor1;
        this.flavor2 = flavor2;
        this.notes = notes;
    }

    public double getPrice() {
        if (flavor2 == null) {
            return flavor1.getPrice(size);
        }
        return Math.max(flavor1.getPrice(size), flavor2.getPrice(size));
    }

    public String getDescription() {
        if (flavor2 == null) {
            return size.getLabel() + " - " + flavor1.getName();
        }
        return size.getLabel() + " - Meio a meio: " + flavor1.getName() + " / " + flavor2.getName();
    }

    public String getNotes() {
        return notes;
    }
}
