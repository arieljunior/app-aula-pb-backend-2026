package org.example.model;

public class Flavor {

    private final int code;
    private final String name;
    private final double smallPrice;
    private final double mediumPrice;
    private final double largePrice;

    public Flavor(int code, String name, double smallPrice, double mediumPrice, double largePrice) {
        this.code = code;
        this.name = name;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice(Size size) {
        return switch (size) {
            case SMALL -> smallPrice;
            case MEDIUM -> mediumPrice;
            case LARGE -> largePrice;
        };
    }
}
