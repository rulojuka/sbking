package br.com.sbk.sbking.core.rulesets.abstractClasses;

@SuppressWarnings("serial")
public abstract class NegativeRuleset extends Ruleset {
	
	@Override
	public boolean isNegative() {
		return true;
	}

}
