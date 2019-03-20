package br.com.sbk.sbking.core.rulesets.abstractClasses;

public abstract class HeartsProhibitableDefaultSuitFollowRuleset extends DefaultSuitFollowRuleset {

	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return true;
	}

}
