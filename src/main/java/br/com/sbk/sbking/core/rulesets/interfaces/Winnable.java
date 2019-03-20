package br.com.sbk.sbking.core.rulesets.interfaces;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Trick;

public interface Winnable {

	public Direction getWinner(Trick trick);

}
