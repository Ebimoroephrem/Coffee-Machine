
package com.coffeemachine;

public enum DrinkType {
    TEA('T'),
    COFFEE('C'),
    HOT_CHOCOLATE('H'),
    ORANGE_JUICE('O');

    private final char code;

    DrinkType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }
}
