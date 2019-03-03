package gui;

import core.rulesets.NegativeKingRuleset;
import core.rulesets.NegativeLastTwoRuleset;
import core.rulesets.NegativeMenRuleset;
import core.rulesets.NegativeTricksRuleset;
import core.rulesets.NegativeWomenRuleset;

public class MainGame {

	public static void main(String args[]) {
		//new GameMode(new NegativeTricksRuleset());
		//new GameMode(new NegativeHeartsRuleset());
		//new GameMode(new NegativeMenRuleset());
		//new GameMode(new NegativeWomenRuleset());
		//new GameMode(new NegativeLastTwoRuleset());
		new GameMode(new NegativeKingRuleset());
	}

}
