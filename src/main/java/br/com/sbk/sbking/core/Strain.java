package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public enum Strain {
	DIAMONDS("Diamonds", new PositiveWithTrumpsRuleset(Suit.DIAMONDS)),
	CLUBS("Clubs", new PositiveWithTrumpsRuleset(Suit.CLUBS)),
	HEARTS("Hearts", new PositiveWithTrumpsRuleset(Suit.HEARTS)),
	SPADES("Spades", new PositiveWithTrumpsRuleset(Suit.SPADES)),
	NOTRUMPS("No Trumps", new PositiveNoTrumpsRuleset());

	private final String name;
	private final PositiveRuleset positiveRuleset;

	private Strain(String name, PositiveRuleset positiveRuleset) {
		this.name = name;
		this.positiveRuleset = positiveRuleset;
	}

	public String getName() {
		return this.name;
	}

	public PositiveRuleset getPositiveRuleset() {
		return positiveRuleset;
	}

}
