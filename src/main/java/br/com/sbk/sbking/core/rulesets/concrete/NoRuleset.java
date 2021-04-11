package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class NoRuleset extends Ruleset {

    public NoRuleset() {
        super();
    }

    @Override
    public int getScoreMultiplier() {
        return 0;
    }

    @Override
    public int getPoints(Trick trick) {
        return 0;
    }

    @Override
    public int getTotalPoints() {
        return 1;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public String getShortDescription() {
        return "No ruleset";
    }

    @Override
    public String getCompleteDescription() {
        return "No ruleset has been decided yet";
    }

}
