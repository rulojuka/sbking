package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;

public class NegativeTricksRuleset extends NegativeRuleset {

	private static final int NEGATIVE_TRICKS_SCORE_MULTIPLIER = 20;
	private static final int NEGATIVE_POINTS_PER_TRICK = 1;

	public NegativeTricksRuleset() {
		this.suitFollowable = new DefaultSuitFollowable();
		this.heartsProhibitable = new DontProhibitsHearts();
		this.winnable = new NoTrumpSuitWinnable();
	}

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

}
