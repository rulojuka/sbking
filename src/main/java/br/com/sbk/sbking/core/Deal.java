package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class Deal {

	private static final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	private Board board;
	private int completedTricks;
	private Direction currentPlayer;
	private Scoreboard scoreboard;

	private Ruleset ruleset;

	private List<Trick> tricks;
	private Trick currentTrick;

	public Deal(Board board, Ruleset ruleset) {
		this.board = board;
		this.ruleset = ruleset;
		currentPlayer = this.board.getLeader();
		this.scoreboard = new Scoreboard(ruleset);
		completedTricks = 0;
		tricks = new ArrayList<Trick>();
	}

	public Hand getHandOf(Direction direction) {
		return this.board.getHandOf(direction);
	}

	public Trick getCurrentTrick() {
		if (currentTrick == null) {
			return new Trick(currentPlayer);
		} else {
			return currentTrick;
		}
	}

	public Direction getCurrentPlayer() {
		return currentPlayer;
	}

	public int getNorthSouthPoints() {
		return this.scoreboard.getNorthSouthPoints();
	}

	public int getEastWestPoints() {
		return this.scoreboard.getEastWestPoints();
	}

	public Ruleset getRuleset() {
		return this.ruleset;
	}

	public boolean isFinished() {
		return this.completedTricks == NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
	}

	public int getCompletedTricks() {
		return this.completedTricks;
	}

	/**
	 * This method will see if playing the card is a valid move. If it is, it will
	 * play it.
	 * 
	 * @param card Card to be played on the board.
	 */
	public void playCard(Card card) {
		Hand handOfCurrentPlayer = getHandOfCurrentPlayer();

		throwExceptionIfCardIsNotFromCurrentPlayer(handOfCurrentPlayer, card);
		throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(card, handOfCurrentPlayer);
		if (currentTrickAlreadyHasCards()) {
			throwExceptionIfCardDoesNotFollowSuit(card, handOfCurrentPlayer);
		}
		if (currentTrickNotStartedYet()) {
			this.currentTrick = startNewTrick();
		}

		moveCardFromHandToCurrentTrick(card, handOfCurrentPlayer);

		if (currentTrick.isComplete()) {
			Direction currentTrickWinner = this.getWinnerOfCurrentTrick();
			currentPlayer = currentTrickWinner;
			updateScoreboard(currentTrickWinner);
			completedTricks++;
		} else {
			currentPlayer = currentPlayer.next();
		}

	}

	private Hand getHandOfCurrentPlayer() {
		return this.board.getHandOf(this.currentPlayer);
	}

	private void throwExceptionIfCardIsNotFromCurrentPlayer(Hand handOfCurrentPlayer, Card card) {
		if (!handOfCurrentPlayer.containsCard(card)) {
			throw new PlayedCardInAnotherPlayersTurnException();
		}
	}

	private void throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(Card card, Hand handOfCurrentPlayer) {
		if (this.currentTrickNotStartedYet() && this.ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.isHeart()
				&& !handOfCurrentPlayer.onlyHasHearts()) {
			throw new PlayedHeartsWhenProhibitedException();
		}
	}

	private boolean currentTrickNotStartedYet() {
		return this.currentTrick == null || this.currentTrick.isEmpty() || this.currentTrick.isComplete();
	}

	private boolean currentTrickAlreadyHasCards() {
		return !currentTrickNotStartedYet();
	}

	private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
		if (!this.ruleset.followsSuit(this.currentTrick, handOfCurrentPlayer, card)) {
			throw new DoesNotFollowSuitException();
		}
	}

	private Trick startNewTrick() {
		Trick currentTrick = new Trick(currentPlayer);
		tricks.add(currentTrick);
		boolean isOneOfLastTwoTricks = completedTricks >= (NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND - 2);
		if (isOneOfLastTwoTricks) {
			currentTrick.setLastTwo();
		}
		return currentTrick;
	}

	private void moveCardFromHandToCurrentTrick(Card card, Hand handOfCurrentPlayer) {
		// FIXME Should be a transaction
		handOfCurrentPlayer.removeCard(card);
		currentTrick.addCard(card);
	}

	private Direction getWinnerOfCurrentTrick() {
		return this.ruleset.getWinner(currentTrick);
	}

	private void updateScoreboard(Direction currentTrickWinner) {
		this.scoreboard.addTrickToDirection(currentTrick, currentTrickWinner);
	}

}
