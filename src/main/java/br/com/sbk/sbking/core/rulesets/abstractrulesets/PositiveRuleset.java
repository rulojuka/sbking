package br.com.sbk.sbking.core.rulesets.abstractrulesets;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;

import br.com.sbk.sbking.core.Trick;

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

    @Override
    public int getTotalPoints() {
        return NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

}
