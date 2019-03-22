package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;

public class NegativeWomenRuleset extends Ruleset {

	private static final int NEGATIVE_WOMEN_SCORE_MULTIPLIER = 50;

	public NegativeWomenRuleset() {
		this.suitFollowable = new DefaultSuitFollowable();
		this.heartsProhibitable = new DontProhibitsHearts();
		this.winnable = new NoTrumpSuitWinnable();
	}

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
