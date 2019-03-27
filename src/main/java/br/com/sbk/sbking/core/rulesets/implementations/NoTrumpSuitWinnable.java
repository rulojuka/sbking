package br.com.sbk.sbking.core.rulesets.implementations;

import java.io.Serializable;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.interfaces.Winnable;

@SuppressWarnings("serial")
public class NoTrumpSuitWinnable implements Winnable, Serializable {

	@Override
	public Direction getWinner(Trick trick) {
		return trick.getWinnerWithoutTrumpSuit();
	}

}
