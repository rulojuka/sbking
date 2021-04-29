package br.com.sbk.sbking.core.rulesets.concrete;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_HEART_KINGS;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;
import br.com.sbk.sbking.core.rulesets.implementations.ProhibitsHearts;

public class NegativeKingRuleset extends NegativeRuleset {

    private static final int NEGATIVE_KING_SCORE_MULTIPLIER = 160;

    public NegativeKingRuleset() {
        super();
        this.suitFollowable = new DefaultSuitFollowable();
        this.heartsProhibitable = new ProhibitsHearts();
        this.winnable = new NoTrumpSuitWinnable();
    }

    @Override
    public int getScoreMultiplier() {
        return NEGATIVE_KING_SCORE_MULTIPLIER;
    }

    @Override
    public int getPoints(Trick trick) {
        if (trick.hasKingOfHearts()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String getShortDescription() {
        return "Negative king";
    }

    @Override
    public String getCompleteDescription() {
        return "Avoid the King of Hearts";
    }

    @Override
    public int getTotalPoints() {
        return NUMBER_OF_HEART_KINGS;
    }

}
