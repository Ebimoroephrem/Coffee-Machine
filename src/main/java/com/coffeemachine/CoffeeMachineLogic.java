
package com.coffeemachine;

public class CoffeeMachineLogic {

    private final DrinkMaker drinkMaker;
    private final Display display;
    private final GenerateRapport generateRapport;
    private final EmailNotifier emailNotifier;
    private final BeverageQuantityChecker quantityChecker;

    public CoffeeMachineLogic(DrinkMaker drinkMaker, Display display) {
        this(drinkMaker, display, null, null, null);
    }

    public CoffeeMachineLogic(
            DrinkMaker drinkMaker,
            Display display,
            GenerateRapport generateRapport,
            EmailNotifier emailNotifier,
            BeverageQuantityChecker quantityChecker) {
        this.drinkMaker = drinkMaker;
        this.display = display;
        this.generateRapport = generateRapport;
        this.emailNotifier = emailNotifier;
        this.quantityChecker = quantityChecker;
    }

    public void process(Order order) {
        int price = getPrice(order.getDrink());
        int money = order.getMoney();

        if (money < price) {
            int missing = price - money;
            drinkMaker.execute("M:Montant insuffisant. Il manque "
                    + missing + " €");
            return;
        }
        String drink = getDrinkName(order.getDrink());

        if (quantityChecker != null && quantityChecker.isEmpty(drink)) {
            display.showMessage(
                    "Rupture de stock : " + drink
                            + ". Une notification a été envoyée.");

            if (emailNotifier != null) {
                emailNotifier.notifierBoissonManquante(drink);
            }

            return;
        }

        drinkMaker.execute(translate(order));

        if (generateRapport != null) {
            generateRapport.recordSale(order.getDrink(), price);
        }
    }

    private int getPrice(DrinkType drink) {
        switch (drink) {
            case TEA:
                return 40;
            case COFFEE:
                return 60;
            case HOT_CHOCOLATE:
                return 50;
            case ORANGE_JUICE:
                return 60;
            default:
                throw new IllegalArgumentException("Boisson inconnue");
        }
    }

    public void onMessage(String message) {
        if (message != null && message.startsWith("M:")) {
            display.showMessage(message.substring(2));
        }
    }

    private String translate(Order order) {
        char drinkCode = order.getDrink().getCode();
        int sugars = order.getSugars();

        if (order.isExtraHot()) {
            switch (order.getDrink()) {
                case TEA:
                    return "Th:" + (sugars == 0 ? "" : sugars)
                            + (sugars == 0 ? ":" : ":0");

                case COFFEE:
                    return "Ch:" + (sugars == 0 ? "" : sugars)
                            + (sugars == 0 ? ":" : ":0");

                case HOT_CHOCOLATE:
                    return "Hh:" + (sugars == 0 ? "" : sugars)
                            + (sugars == 0 ? ":" : ":0");

                case ORANGE_JUICE:
                    throw new IllegalArgumentException(
                            "Le jus d'orange ne peut pas être extra chaud");
            }
        }

        if (sugars == 0) {
            return drinkCode + "::";
        }

        return drinkCode + ":" + sugars + ":0";
    }

    private String getDrinkName(DrinkType drink) {
        switch (drink) {
            case TEA:
                return "tea";
            case COFFEE:
                return "coffee";
            case HOT_CHOCOLATE:
                return "chocolate";
            case ORANGE_JUICE:
                return "orange juice";
            default:
                throw new IllegalArgumentException(
                        "Boisson inconnue : " + drink);
        }
    }

}
