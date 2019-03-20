package br.com.sbk.sbking.core.rulesets.implementations;

import br.com.sbk.sbking.core.rulesets.interfaces.HeartsProhibitable;

public class ProhibitsHearts implements HeartsProhibitable {

	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return true;
	}

}
