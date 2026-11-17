package org.example.repository;

import org.example.model.Flavor;

import java.util.ArrayList;
import java.util.List;

public class PizzaMenu {

    private final List<Flavor> flavors = new ArrayList<>();

    public PizzaMenu() {
        flavors.add(new Flavor(1, "Mussarela", 35.00, 45.00, 55.00));
        flavors.add(new Flavor(2, "Calabresa", 38.00, 48.00, 58.00));
        flavors.add(new Flavor(3, "Portuguesa", 40.00, 52.00, 62.00));
        flavors.add(new Flavor(4, "Frango com Catupiry", 42.00, 54.00, 64.00));
        flavors.add(new Flavor(5, "Quatro Queijos", 45.00, 58.00, 68.00));
    }

    public List<Flavor> listFlavors() {
        return flavors;
    }

    public Flavor findByCode(int code) {
        for (Flavor flavor : flavors) {
            if (flavor.getCode() == code) {
                return flavor;
            }
        }
        return null;
    }
}
