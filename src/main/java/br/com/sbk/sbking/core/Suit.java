package br.com.sbk.sbking.core;

public enum Suit {
    DIAMONDS("Diamonds", "d", '\u2666'), CLUBS("Clubs", "c", '\u2663'), HEARTS("Hearts", "h", '\u2665'),
    SPADES("Spades", "s", '\u2660');

    private final String name;
    private final String symbol;
    private final char unicodeSymbol;

    Suit(String name, String symbol, char unicodeSymbol) {
        this.name = name;
        this.symbol = symbol;
        this.unicodeSymbol = unicodeSymbol;
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

}
