package br.com.sbk.sbking.core;

public enum Strain {
	DIAMONDS("Diamonds", "d"), CLUBS("Clubs", "c"), HEARTS("Hearts", "h"), SPADES("Spades", "s"), NOTRUMPS("No Trumps", "n");

	private final String name;
	private final String symbol;

	private Strain(String name, String symbol) {
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
