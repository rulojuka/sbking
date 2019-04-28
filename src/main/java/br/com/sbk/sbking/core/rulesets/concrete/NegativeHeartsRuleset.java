package br.com.sbk.sbking.core.rulesets.concrete;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_HEARTS_CARDS;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;
import br.com.sbk.sbking.core.rulesets.implementations.ProhibitsHearts;

@SuppressWarnings("serial")
public class NegativeHeartsRuleset extends NegativeRuleset {

	private static final int NEGATIVE_HEARTS_SCORE_MULTIPLIER = 20;

	public NegativeHeartsRuleset() {
		this.suitFollowable = new DefaultSuitFollowable();
		this.heartsProhibitable = new ProhibitsHearts();
		this.winnable = new NoTrumpSuitWinnable();
	}

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_HEARTS_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		return trick.getNumberOfHeartsCards();
	}

	@Override
	public String getShortDescription() {
		return "Negative hearts";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid all hearts cards";
	}

	@Override
	public int getTotalPoints() {
		return NUMBER_OF_HEARTS_CARDS;
	}

}
