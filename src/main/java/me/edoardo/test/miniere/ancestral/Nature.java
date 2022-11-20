package me.edoardo.test.miniere.ancestral;

import java.util.Random;

public enum Nature {
    VERDE("㊀","#1e5e00"),
    ROSSO("㊅","#5e0000"),
    BLU("㊇","#00355e"),
    GIALLO("㊂","#5e4f00");

    private final String symbol;
    private final String color;

    Nature(String symbol,String color) {
        this.symbol = symbol;
        this.color = color;
    }

    public static Nature getRandom(Random random) {
        return Nature.values()[random.nextInt(Nature.values().length)];
    }

    public String getColor() {
        return color;
    }

    public String getSymbol() {
        return symbol;
    }
}
