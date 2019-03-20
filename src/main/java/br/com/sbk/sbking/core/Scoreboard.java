package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class Scoreboard {

	private int northSouthPoints = 0;
	private int eastWestPoints = 0;
	private Ruleset ruleset;

	public Scoreboard(Ruleset ruleset) {
		this.ruleset = ruleset;
	}

	public int getNorthSouthPoints() {
		return northSouthPoints;
	}

	public int getEastWestPoints() {
		return eastWestPoints;
	}
	
	public void addTrickToDirection(Trick trick, Direction winner) {
		if(winner.isNorthSouth()) {
			addNorthSouth(trick);
		}
		else {
			addEastWest(trick);
		}
	}

	private void addNorthSouth(Trick trick) {
		northSouthPoints += this.ruleset.getPoints(trick);
	}

	private void addEastWest(Trick trick) {
		eastWestPoints += this.ruleset.getPoints(trick);
	}

}
