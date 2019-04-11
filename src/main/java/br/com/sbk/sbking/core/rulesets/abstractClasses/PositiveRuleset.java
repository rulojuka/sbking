package br.com.sbk.sbking.core.rulesets.abstractClasses;

import br.com.sbk.sbking.core.Trick;

@SuppressWarnings("serial")
public abstract class PositiveRuleset extends Ruleset {

	private static final int POSITIVE_SCORE_MULTIPLIER = 25;
	private static final int POSITIVE_POINTS_PER_TRICK = 1;

	@Override
	public int getScoreMultiplier() {
		return POSITIVE_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		return POSITIVE_POINTS_PER_TRICK;
	}

}
