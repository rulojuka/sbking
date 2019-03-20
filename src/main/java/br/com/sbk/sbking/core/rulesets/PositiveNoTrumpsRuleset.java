package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;

public class PositiveNoTrumpsRuleset extends PositiveRuleset {

	@Override
	public String getShortDescription() {
		return "Positive no trumps";
	}

	@Override
	public String getCompleteDescription() {
		return "Make the most tricks without a trump suit";
	}

}
