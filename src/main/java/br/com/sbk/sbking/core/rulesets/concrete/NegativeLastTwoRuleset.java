package br.com.sbk.sbking.core.rulesets.concrete;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_LAST_TWO_TRICKS;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;

public class NegativeLastTwoRuleset extends NegativeRuleset {

    private static final int NEGATIVE_LAST_TWO_SCORE_MULTIPLIER = 90;

    public NegativeLastTwoRuleset() {
        super();
        this.suitFollowable = new DefaultSuitFollowable();
        this.heartsProhibitable = new DontProhibitsHearts();
        this.winnable = new NoTrumpSuitWinnable();
    }

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
    public int getTotalPoints() {
        return NUMBER_OF_LAST_TWO_TRICKS;
    }

}
