package core.rulesets;

import core.Trick;

public class NegativeWomenRuleset implements Ruleset {

	private final int NEGATIVE_WOMEN_SCORE_MULTIPLIER = 50;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_WOMEN_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		return trick.getNumberOfWomen();
	}

	@Override
	public String getShortDescription() {
		return "Negative women";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid all queens";
	}

}
