package br.com.sbk.sbking.core.rulesets.implementations;

import java.io.Serializable;

import br.com.sbk.sbking.core.rulesets.interfaces.HeartsProhibitable;

@SuppressWarnings("serial")
public class ProhibitsHearts implements HeartsProhibitable, Serializable {

    @Override
    public boolean prohibitsHeartsUntilOnlySuitLeft() {
        return true;
    }

}
