package br.com.sbk.sbking.core.rulesets.concrete;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_MEN;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;

public class NegativeMenRuleset extends NegativeRuleset {

    private static final int NEGATIVE_MEN_SCORE_MULTIPLIER = 30;

    public NegativeMenRuleset() {
        super();
        this.suitFollowable = new DefaultSuitFollowable();
        this.heartsProhibitable = new DontProhibitsHearts();
        this.winnable = new NoTrumpSuitWinnable();
    }

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

    @Override
    public int getTotalPoints() {
        return NUMBER_OF_MEN;
    }

}
