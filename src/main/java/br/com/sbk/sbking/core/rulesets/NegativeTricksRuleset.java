package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;

public class NegativeTricksRuleset implements Ruleset {

	private final int NEGATIVE_TRICKS_SCORE_MULTIPLIER = 20;
	private final int NEGATIVE_POINTS_PER_TRICK = 1;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_TRICKS_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		return NEGATIVE_POINTS_PER_TRICK;
	}

	@Override
	public String getShortDescription() {
		return "Negative tricks";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid all tricks";
	}
	
	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return false;
	}

}
