package br.com.sbk.sbking.core.rulesets.interfaces;

import br.com.sbk.sbking.core.Trick;

public interface Scoreable {

	public int getScoreMultiplier();

	public int getPoints(Trick trick);
	
	public int getTotalPoints();
	
	public boolean isNegative();

}
