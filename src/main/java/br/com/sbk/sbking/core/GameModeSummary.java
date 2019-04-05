package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class GameModeSummary {

	private Direction chosenBy;
	private Ruleset ruleset;
	private Integer orderOfPlay;
	private Score score;

	public GameModeSummary(Direction chosenBy, Ruleset ruleset, int orderOfPlay, Score score) {
		super();
		this.chosenBy = chosenBy;
		this.ruleset = ruleset;
		this.orderOfPlay = orderOfPlay;
		this.score = score;
	}

	public String getName() {
		return this.ruleset.getShortDescription();
	}

	public String getScore() {
		return score.getSummary();
	}

	public String getChosenBy() {
		if (chosenBy.isNorthSouth()) {
			return "N/S";
		} else {
			return "E/W";
		}
	}

	public String getOrderOfPlay() {
		return orderOfPlay.toString();
	}

}
