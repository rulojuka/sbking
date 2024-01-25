package br.com.sbk.sbking.core;

public enum OddTricks {

    ONE("One", "1"), TWO("Two", "2"), THREE("Three", "3"), FOUR("Four", "4"), FIVE("Five", "5"), SIX("Six", "6"), SEVEN("Seven", "7");

    private final String name;
    private final String symbol;

    OddTricks(String name, String symbol) {
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
