package br.com.sbk.sbking.core;

public enum OddTricks {

    ONE("One", "1", 1), TWO("Two", "2", 2), THREE("Three", "3", 3), FOUR("Four", "4", 4), FIVE("Five", "5", 5), SIX("Six", "6", 6),
    SEVEN("Seven", "7", 7);

    private final String name;
    private final String symbol;
    private final int level;

    OddTricks(String name, String symbol, int level) {
        this.name = name;
        this.symbol = symbol;
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public int getLevel() {
        return this.level;
    }

}
