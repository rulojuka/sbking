package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Suit;

public class PositiveWithTrumpsRuleset extends PositiveRuleset {
	
	private Suit trumpSuit;

	public PositiveWithTrumpsRuleset(Suit trumpSuit) {
		this.trumpSuit = trumpSuit;
	}

	@Override
	public String getShortDescription() {
		return "Positive " + trumpSuit.getName();
	}

	@Override
	public String getCompleteDescription() {
		return "Make the most tricks with " + trumpSuit.getName() + " as trump suit";
	}

	public Suit getTrumpSuit() {
		return trumpSuit;
	}

}
