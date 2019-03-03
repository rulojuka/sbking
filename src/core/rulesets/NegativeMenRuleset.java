package core.rulesets;

import core.Card;
import core.Trick;

public class NegativeMenRuleset implements Ruleset {

	private final int NEGATIVE_MEN_SCORE_MULTIPLIER = 30;
	
	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_MEN_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		int trickPoints = 0;
		for(Card c : trick.getTrickCards()) {
			if(c.isMan()) {
				trickPoints ++;
			}
		}
		return trickPoints;
	}

	@Override
	public String getShortDescription() {
		return "Negative Men";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid all jacks and kings";
	}

}
