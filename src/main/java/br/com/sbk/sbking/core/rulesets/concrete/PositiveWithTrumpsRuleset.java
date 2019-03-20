package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.TrumpSuitWinnable;

public class PositiveWithTrumpsRuleset extends PositiveRuleset {

	private Suit trumpSuit;

	public PositiveWithTrumpsRuleset(Suit trumpSuit) {
		this.suitFollowable = new DefaultSuitFollowable();
		this.heartsProhibitable = new DontProhibitsHearts();
		this.winnable = new TrumpSuitWinnable(trumpSuit);
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

}
