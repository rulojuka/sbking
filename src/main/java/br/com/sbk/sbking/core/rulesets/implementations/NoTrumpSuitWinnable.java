package br.com.sbk.sbking.core.rulesets.implementations;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.interfaces.Winnable;

public class NoTrumpSuitWinnable implements Winnable{

	@Override
	public Direction getWinner(Trick trick) {
		return trick.getWinnerWithoutTrumpSuit();
	}

}
