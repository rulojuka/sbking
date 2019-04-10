package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public enum Strain {
	DIAMONDS("Diamonds", "d", new PositiveWithTrumpsRuleset(Suit.DIAMONDS)),
	CLUBS("Clubs", "c", new PositiveWithTrumpsRuleset(Suit.CLUBS)),
	HEARTS("Hearts", "h", new PositiveWithTrumpsRuleset(Suit.HEARTS)),
	SPADES("Spades", "s", new PositiveWithTrumpsRuleset(Suit.SPADES)),
	NOTRUMPS("No Trumps", "n", new PositiveNoTrumpsRuleset());

	private final String name;
	private final String symbol;
	private final PositiveRuleset positiveRuleset;

	private Strain(String name, String symbol, PositiveRuleset positiveRuleset) {
		this.name = name;
		this.symbol = symbol;
		this.positiveRuleset = positiveRuleset;
	}

	public String getName() {
		return this.name;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public PositiveRuleset getPositiveRuleset() {
		return positiveRuleset;
	}

}
