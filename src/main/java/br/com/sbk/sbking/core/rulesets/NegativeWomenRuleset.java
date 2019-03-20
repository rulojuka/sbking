package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NonHeartsProhibitableDefaltSuitFollowRuleset;

public class NegativeWomenRuleset extends NonHeartsProhibitableDefaltSuitFollowRuleset {

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
