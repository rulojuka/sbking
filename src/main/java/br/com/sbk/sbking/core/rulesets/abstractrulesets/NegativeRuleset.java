package br.com.sbk.sbking.core.rulesets.abstractrulesets;

public abstract class NegativeRuleset extends Ruleset {

    public NegativeRuleset() {
        super();
    }

    @Override
    public boolean isNegative() {
        return true;
    }

}
