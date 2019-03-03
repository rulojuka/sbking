package gui;

import core.rulesets.NegativeMenRuleset;

public class MainGame {

	public static void main(String args[]) {
		new GameMode(new NegativeMenRuleset());
	}

}
