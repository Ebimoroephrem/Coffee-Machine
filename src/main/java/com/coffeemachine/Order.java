
package com.coffeemachine;

public class Order {
    private final DrinkType drink;
    private final int sugars;
    private final int money;
    private final boolean extraHot;

    public Order(DrinkType drink, int sugars, int money) {
        this(drink, sugars, money, false);
    }

    public Order(DrinkType drink, int sugars, int money, boolean extraHot) {
        this.drink = drink;
        this.sugars = sugars;
        this.money = money;
        this.extraHot = extraHot;
    }

    public DrinkType getDrink() {
        return drink;
    }

    public int getSugars() {
        return sugars;
    }

    public int getMoney() {
        return money;
    }
    // Indique si la boisson est extra chaude
    public boolean isExtraHot() {
        return extraHot;
    }
    // Batonnet de sucre est présent si le nombre de sucres est supérieur à 0
    public boolean withStick() {
        return sugars > 0;
    }
}
