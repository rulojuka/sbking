package core.rulesets;

import core.Suit;

public class PositiveWithTrumpRuleset extends PositiveRuleset {
	
	private Suit trumpSuit;

	public PositiveWithTrumpRuleset(Suit trumpSuit) {
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
