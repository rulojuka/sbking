package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.Ruleset;

public class Deal {

	private final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	private Board board;
	private Trick currentTrick;
	private int completedTricks;
	private Direction currentPlayer;
	private int northSouthPoints;
	private int eastWestPoints;
	private Ruleset ruleset;
	private Suit trumpSuit;

	private Direction currentTrickWinner;

	public Deal(Board board, Ruleset ruleset) {
		this.board = board;
		this.ruleset = ruleset;
		currentPlayer = this.board.getLeader();
		currentTrick = new Trick(currentPlayer);
		northSouthPoints = 0;
		eastWestPoints = 0;
		completedTricks = 0;
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
		return currentTrick;
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

	/**
	 * This method will see if playing the card is a valid move. If it is, it will
	 * play it.
	 * 
	 * @param card Card to be played on the board.
	 */
	public void playCard(Card card) {
		if (!playedCardIsFromCurrentPlayer(card)) {
			throw new RuntimeException("Trying to play in another players turn.");
		}
		if (currentTrick.isComplete()) {
			currentTrick = new Trick(currentPlayer);
			if (completedTricks >= (NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND - 2)) {
				currentTrick.setLastTwo();
			}
		}
		Hand handOfCurrentPlayer = getHandOfCurrentPlayer();
		if (currentTrick.isEmpty() && ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.getSuit() == Suit.HEARTS
				&& !handOfCurrentPlayer.onlyHasHearts()) {
			throw new RuntimeException("Ruleset prohibits playing hearts until only suit left.");
		}
		if (!followsSuit(card, handOfCurrentPlayer)) {
			throw new RuntimeException("Card does not follow suit.");
		}

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

	private boolean playedCardIsFromCurrentPlayer(Card card) {
		return this.getHandOfCurrentPlayer().containsCard(card);
	}

	private Hand getHandOfCurrentPlayer() {
		return this.board.getHandOf(this.currentPlayer);
	}

	private boolean followsSuit(Card card, Hand hand) {
		Trick trick = this.getCurrentTrick();
		if (trick.isEmpty())
			return true;
		Suit leadSuit = trick.getLeadSuit();

		if (hand.hasSuit(leadSuit) == false) {
			return true;
		} else if (card.getSuit() == leadSuit) {
			return true;
		}

		return false;
	}

	private void updatePoints() {
		if (currentTrickWinner.isNorthSouth()) {
			northSouthPoints += this.ruleset.getPoints(currentTrick);
		} else {
			eastWestPoints += this.ruleset.getPoints(currentTrick);
		}
	}

}
