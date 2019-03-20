package br.com.sbk.sbking.core.rulesets.abstractClasses;

public abstract class NonHeartsProhibitableDefaltSuitFollowRuleset extends DefaultSuitFollowRuleset {

	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return false;
	}

}
