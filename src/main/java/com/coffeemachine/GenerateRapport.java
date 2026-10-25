package com.coffeemachine;

import java.util.EnumMap;
import java.util.Map;

public class GenerateRapport {

    private final Map<DrinkType, Integer> sales = new EnumMap<>(DrinkType.class);

    private int totalRevenue;

    public GenerateRapport() {
        for (DrinkType drink : DrinkType.values()) {
            sales.put(drink, 0);
        }
        totalRevenue = 0;
    }

    public int getSoldCount(DrinkType drink) {
        return sales.get(drink);
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public void recordSale(DrinkType drink, int price) {
        sales.put(drink, sales.get(drink) + 1);
        totalRevenue += price;
    }

    public void printReport() {
        System.out.println("Rapport des ventes");

        for (DrinkType drink : DrinkType.values()) {
            System.out.println(
                    getDrinkName(drink) + " : " + getSoldCount(drink));
        }

        System.out.printf(
                java.util.Locale.US,
                "Recettes totales : %.2f EUR%n",
                totalRevenue / 100.0);
    }

    private String getDrinkName(DrinkType drink) {
        switch (drink) {
            case TEA:
                return "Thé";
            case COFFEE:
                return "Café";
            case HOT_CHOCOLATE:
                return "Chocolat chaud";
            case ORANGE_JUICE:
                return "Jus d'orange";
            default:
                return drink.name();
        }
    }

}
