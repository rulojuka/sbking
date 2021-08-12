package br.com.sbk.sbking.core;

import java.awt.Color;

import br.com.sbk.sbking.gui.constants.FrameConstants;

public enum Suit {
    DIAMONDS("Diamonds", "d", '\u2666', FrameConstants.RED_SUIT_COLOR),
    CLUBS("Clubs", "c", '\u2663', FrameConstants.BLACK_SUIT_COLOR),
    HEARTS("Hearts", "h", '\u2665', FrameConstants.RED_SUIT_COLOR),
    SPADES("Spades", "s", '\u2660', FrameConstants.BLACK_SUIT_COLOR);

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
