package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
import static br.com.sbk.sbking.core.GameConstants.MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
import static br.com.sbk.sbking.core.GameConstants.TOTAL_GAMES;

import java.util.HashSet;
import java.util.Set;

import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.GameScoreboard;

public class Game {

	private GameScoreboard gameScoreboard;
	private Set<NegativeRuleset> chosenNegativeRulesets = new HashSet<NegativeRuleset>();
	private int northSouthNegatives = 0;
	private int eastWestNegatives = 0;
	private int northSouthPositives = 0;
	private int eastWestPositives = 0;
	private int playedHands = 0;

	private Board currentBoard;
	private Deal currentDeal;

	private Direction dealer = Direction.NORTH;

	public Game() {
		this.gameScoreboard = new GameScoreboard();
	}

	public void dealNewBoard() {
		BoardDealer boardDealer = new BoardDealer();
		this.currentBoard = boardDealer.dealBoard(this.dealer, new ShuffledDeck());
	}

	public void addRuleset(Ruleset currentGameModeOrStrain) {
		this.currentDeal = new Deal(this.currentBoard, currentGameModeOrStrain);
	}

	public boolean isFinished() {
		return playedHands == TOTAL_GAMES;
	}

	public void finishDeal() {
		Direction currentChooser = this.getCurrentDeal().getDealer().getPositiveOrNegativeChooserWhenDealer();
		boolean isNorthSouth = currentChooser.isNorthSouth();
		Ruleset currentRuleset = this.getCurrentDeal().getRuleset();
		boolean isPositive = currentRuleset instanceof PositiveRuleset;

		this.getGameScoreboard().addFinishedDeal(this.getCurrentDeal());
		if (isPositive) {
			if (isNorthSouth) {
				northSouthPositives++;
			} else {
				eastWestPositives++;
			}
		} else {
			NegativeRuleset negativeRuleset = (NegativeRuleset) currentRuleset;
			this.chosenNegativeRulesets.add(negativeRuleset);
			if (isNorthSouth) {
				northSouthNegatives++;
			} else {
				eastWestNegatives++;
			}
		}
		this.dealer = this.dealer.next();
		this.playedHands++;
	}

	public GameScoreboard getGameScoreboard() {
		return gameScoreboard;
	}

	public boolean isGameModePermitted(Ruleset ruleset, Direction chooser) {
		boolean positiveRuleset = (ruleset instanceof PositiveRuleset);
		boolean chooserNorthSouth = chooser.isNorthSouth();

		if (!positiveRuleset && chosenNegativeRulesets.contains(ruleset)) {
			return false;
		}

		if (chooserNorthSouth) {
			if (positiveRuleset) {
				return northSouthPositives < MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
			} else {
				return northSouthNegatives < MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
			}
		} else {
			if (positiveRuleset) {
				return eastWestPositives < MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
			} else {
				return eastWestNegatives < MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
			}
		}

	}

	public Direction getDealer() {
		return this.dealer;
	}

	public Board getCurrentBoard() {
		return this.currentBoard;
	}

	public Deal getCurrentDeal() {
		return this.currentDeal;
	}

}
