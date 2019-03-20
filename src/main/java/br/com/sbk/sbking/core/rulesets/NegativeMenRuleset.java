package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NonHeartsProhibitableDefaltSuitFollowRuleset;

public class NegativeMenRuleset extends NonHeartsProhibitableDefaltSuitFollowRuleset {

	private final int NEGATIVE_MEN_SCORE_MULTIPLIER = 30;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_MEN_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		return trick.getNumberOfMen();
	}

	@Override
	public String getShortDescription() {
		return "Negative men";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid all jacks and kings";
	}

}
