package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;

public class PositiveNoTrumpsRuleset extends PositiveRuleset {

    public PositiveNoTrumpsRuleset() {
        super();
        this.suitFollowable = new DefaultSuitFollowable();
        this.heartsProhibitable = new DontProhibitsHearts();
        this.winnable = new NoTrumpSuitWinnable();
    }

    @Override
    public String getShortDescription() {
        return "Positive no trumps";
    }

    @Override
    public String getCompleteDescription() {
        return "Make the most tricks without a trump suit";
    }

}
