package br.com.sbk.sbking.core;

public enum SkatRank {
    SEVEN("Seven", "7"), EIGHT("Eight", "8"), NINE("Nine", "9"), TEN("Ten", "T"),
    JACK("Jack", "B"), QUEEN("Queen", "D"), KING("King", "K"), ACE("Ace", "A");

    private final String name;
	private final String symbol;

	private SkatRank(String name, String symbol) {
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
