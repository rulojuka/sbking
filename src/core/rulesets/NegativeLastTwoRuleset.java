package core.rulesets;

import core.Trick;

public class NegativeLastTwoRuleset implements Ruleset {

	private final int NEGATIVE_LAST_TWO_SCORE_MULTIPLIER = 90;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_LAST_TWO_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		if (trick.isLastTwo()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String getShortDescription() {
		return "Negative last two";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid the last two tricks";
	}
	
	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return false;
	}

}
