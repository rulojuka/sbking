package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;

public enum NegativeRulesetsEnum {

    TRICKS(new NegativeTricksRuleset()), HEARTS(new NegativeHeartsRuleset()), MEN(new NegativeMenRuleset()),
    WOMEN(new NegativeWomenRuleset()), LASTTWO(new NegativeLastTwoRuleset()), KING(new NegativeKingRuleset());

    private final NegativeRuleset negativeRuleset;

    private NegativeRulesetsEnum(NegativeRuleset ruleset) {
        this.negativeRuleset = ruleset;
    }

    public NegativeRuleset getNegativeRuleset() {
        return negativeRuleset;
    }

}
