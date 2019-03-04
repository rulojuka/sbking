package br.com.sbk.sbking.core;

public enum Rank {
	TWO("Two", "2"), THREE("Three", "3"), FOUR("Four", "4"), FIVE("Five", "5"), SIX("Six", "6"), SEVEN("Seven", "7"),
	EIGHT("Eight", "8"), NINE("Nine", "9"), TEN("Ten", "T"), JACK("Jack", "J"), QUEEN("Queen", "Q"), KING("King", "K"),
	ACE("Ace", "A");

	private final String name;
	private final String symbol;

	private Rank(String name, String symbol) {
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
