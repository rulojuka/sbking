package core.rulesets;

import core.Trick;

public interface Ruleset {
	
	public int getScoreMultiplier();
	
	public int getPoints(Trick trick);
	
	public String getShortDescription();
	
	public String getCompleteDescription();

	public boolean prohibitsHeartsUntilOnlySuitLeft();

}
