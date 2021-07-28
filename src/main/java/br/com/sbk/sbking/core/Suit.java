package br.com.sbk.sbking.core;

import java.awt.Color;

public enum Suit {
    DIAMONDS("Diamonds", "d", '\u2666', new Color(255, 0, 0)), CLUBS("Clubs", "c", '\u2663', new Color(0, 0, 0)),
    HEARTS("Hearts", "h", '\u2665', new Color(255, 0, 0)), SPADES("Spades", "s", '\u2660', new Color(0, 0, 0));

    private final String name;
    private final String symbol;
    private final char unicodeSymbol;
    private final Color color;

    Suit(String name, String symbol, char unicodeSymbol, Color color) {
        this.name = name;
        this.symbol = symbol;
        this.unicodeSymbol = unicodeSymbol;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public char getUnicodeSymbol() {
        return this.unicodeSymbol;
    }

    public Color getColor() {
        return this.color;
    }

}
