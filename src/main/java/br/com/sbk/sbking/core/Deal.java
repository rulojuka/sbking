package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsInANegativeHeartsWithOtherSuitsLeft;
import br.com.sbk.sbking.core.rulesets.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.interfaces.SuitFollowable;

public class Deal {

	private final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	private Board board;
	private int completedTricks;
	private Direction currentPlayer;
	private int northSouthPoints;
	private int eastWestPoints;
	private Ruleset ruleset;
	private Suit trumpSuit;

	private Direction currentTrickWinner;

	private List<Trick> tricks;
	private Trick currentTrick;
	private SuitFollowable suitFollowRule;

	public Deal(Board board, Ruleset ruleset) {
		this.board = board;
		this.ruleset = ruleset;
		currentPlayer = this.board.getLeader();
		northSouthPoints = 0;
		eastWestPoints = 0;
		completedTricks = 0;
		tricks = new ArrayList<Trick>();
		if (ruleset instanceof PositiveWithTrumpsRuleset) {
			PositiveWithTrumpsRuleset positiveWithTrumpRuleset = (PositiveWithTrumpsRuleset) ruleset;
			this.trumpSuit = positiveWithTrumpRuleset.getTrumpSuit();
		} else {
			this.trumpSuit = null;
		}
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
		return northSouthPoints;
	}

	public int getEastWestPoints() {
		return eastWestPoints;
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
		startNewTrickIfCurrentTrickIsUninitialized();
		throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(card, handOfCurrentPlayer);
		throwExceptionIfCardDoesNotFollowSuit(card, handOfCurrentPlayer);

		currentTrick.addCard(card);
		handOfCurrentPlayer.removeCard(card);

		if (currentTrick.isComplete()) {
			if (this.trumpSuit == null) {
				currentTrickWinner = currentTrick.getWinnerWithoutTrumpSuit();
			} else {
				currentTrickWinner = currentTrick.getWinnerWithTrumpSuit(trumpSuit);
			}
			completedTricks++;
			updatePoints();
			currentPlayer = currentTrickWinner;
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

	private void startNewTrickIfCurrentTrickIsUninitialized() {
		if (this.currentTrick == null) {
			this.currentTrick = startNewTrick();
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

	private void throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(Card card, Hand handOfCurrentPlayer) {
		if (this.currentTrick.isEmpty() && this.ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.isHeart()
				&& !handOfCurrentPlayer.onlyHasHearts()) {
			throw new PlayedHeartsInANegativeHeartsWithOtherSuitsLeft();
		}
	}

	private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
		if (!followsSuit(card, handOfCurrentPlayer)) {
			throw new RuntimeException("Card does not follow suit.");
		}
	}

	private boolean followsSuit(Card card, Hand hand) {
		if (this.currentTrick.isEmpty())
			return true;

		return this.suitFollowRule.followsSuit(this.currentTrick, hand, card);
	}

	private void updatePoints() {
		if (currentTrickWinner.isNorthSouth()) {
			northSouthPoints += this.ruleset.getPoints(currentTrick);
		} else {
			eastWestPoints += this.ruleset.getPoints(currentTrick);
		}
	}

}
