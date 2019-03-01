package core;

public enum Suit {
	// Suit order used to sort cards, not used in the game rules
	DIAMONDS("Diamonds", "d"), CLUBS("Clubs", "c"), HEARTS("Hearts", "h"), SPADES("Spades", "s");

	private final String name;
	private final String symbol;

	private Suit(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

}
