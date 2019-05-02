package br.com.sbk.sbking.core;

import java.io.Serializable;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

@SuppressWarnings("serial")
public class GameModeSummary implements Serializable {

	private Direction chosenBy;
	private Ruleset ruleset;
	private Integer orderOfPlay;
	private Score score;

	public GameModeSummary(Direction chosenBy, Ruleset ruleset, int orderOfPlay, Score score) {
		this.chosenBy = chosenBy;
		this.ruleset = ruleset;
		this.orderOfPlay = orderOfPlay;
		this.score = score;
	}

	public String getName() {
		return this.ruleset.getShortDescription();
	}

	public int getScore() {
		return score.getFinalPunctuation();
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
