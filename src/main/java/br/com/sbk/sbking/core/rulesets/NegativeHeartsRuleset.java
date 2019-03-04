package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;

public class NegativeHeartsRuleset implements Ruleset {

	@Override
	public int getScoreMultiplier() {
		return 20;
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
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return true;
	}
}
