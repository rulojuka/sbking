package core.rulesets;

import core.Trick;

public abstract class PositiveRuleset implements Ruleset {
	
	private final int POSITIVE_SCORE_MULTIPLIER = 25;
	private final int POSITIVE_POINTS_PER_TRICK = 1;

	@Override
	public int getScoreMultiplier() {
		return POSITIVE_SCORE_MULTIPLIER;
	}
	
	@Override
	public int getPoints(Trick trick) {
		return POSITIVE_POINTS_PER_TRICK;
	}

	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return false;
	}
	
}
