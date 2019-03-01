package core;

import java.util.*;

public final class Suit implements Comparable<Suit> {
	private String name;
	private String symbol;

	public final static Suit CLUBS = new Suit("Clubs", "c");
	public final static Suit DIAMONDS = new Suit("Diamonds", "d");
	public final static Suit HEARTS = new Suit("Hearts", "h");
	public final static Suit SPADES = new Suit("Spades", "s");

	public final static java.util.List VALUES = Collections
			.unmodifiableList(Arrays.asList(new Suit[] { DIAMONDS, CLUBS, HEARTS, SPADES }));

	private Suit(String nameValue, String symbolValue) {
		name = nameValue;
		symbol = symbolValue;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Suit otherSuit) { /* Used to sort cards, not used in the game rules */
		return VALUES.indexOf(otherSuit) - VALUES.indexOf(this);
	}

}
