package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;

public interface Ruleset {
	
	public int getScoreMultiplier();
	
	public int getPoints(Trick trick);
	
	public String getShortDescription();
	
	public String getCompleteDescription();

	public boolean prohibitsHeartsUntilOnlySuitLeft();

}
