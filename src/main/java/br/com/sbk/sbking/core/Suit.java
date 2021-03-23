package br.com.sbk.sbking.core;

public enum Suit {
    DIAMONDS("Diamonds", "d"), CLUBS("Clubs", "c"), HEARTS("Hearts", "h"), SPADES("Spades", "s");

    private final String name;
    private final String symbol;

    private Suit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

}
