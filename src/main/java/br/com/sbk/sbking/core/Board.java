package br.com.sbk.sbking.core;

import java.util.List;

import br.com.sbk.sbking.core.rulesets.PositiveWithTrumpRuleset;
import br.com.sbk.sbking.core.rulesets.Ruleset;

public class Board {

	private final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Trick currentTrick;
	private int completedTricks;
	private Direction currentPlayer;
	private int northSouthPoints;
	private int eastWestPoints;
	private Ruleset ruleset;
	private Suit trumpSuit;

	public Board(List<Hand> hands, Ruleset ruleset) {
		this.northHand = hands.get(0);
		this.eastHand = hands.get(1);
		this.southHand = hands.get(2);
		this.westHand = hands.get(3);
		currentTrick = new Trick();
		currentPlayer = Direction.NORTH;
		northSouthPoints = 0;
		eastWestPoints = 0;
		completedTricks = 0;
		this.ruleset = ruleset;
		if (ruleset instanceof PositiveWithTrumpRuleset) {
			PositiveWithTrumpRuleset positiveWithTrumpRuleset = (PositiveWithTrumpRuleset) ruleset;
			this.trumpSuit = positiveWithTrumpRuleset.getTrumpSuit();
		}
		sortHands();
	}

	public Hand getNorthHand() {
		return northHand;
	}

	public Hand getEastHand() {
		return eastHand;
	}

	public Hand getSouthHand() {
		return southHand;
	}

	public Hand getWestHand() {
		return westHand;
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
			currentTrick.discard();
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
		if (currentTrick.isEmpty()) {
			currentTrick.setLeader(currentPlayer);
			currentTrick.setTrumpSuit(trumpSuit);
		}

		currentTrick.addCard(card);
		getHandOfCurrentPlayer().removeCard(card);

		if (currentTrick.isComplete()) {
			Direction winner = currentTrick.getWinner();
			completedTricks++;
			updatePoints();
			currentPlayer = winner;
		} else {
			currentPlayer = currentPlayer.next();
		}

	}

	/**
	 * 
	 * @param card Card that is going to be be validated
	 * @param hand Hand of the player that is playing that card
	 * @return True if the card that is being played follow the basic trick rules.
	 *         False if it does not.
	 */
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
		if (currentTrick.isComplete()) {
			Direction winner = currentTrick.getWinner();
			if (winner.isNorthSouth()) {
				northSouthPoints += this.ruleset.getPoints(currentTrick);
			} else {
				eastWestPoints += this.ruleset.getPoints(currentTrick);
			}
		}
	}

	private boolean playedCardIsFromCurrentPlayer(Card card) {
		return (currentPlayer.isNorth() && northHand.containsCard(card))
				|| (currentPlayer.isEast() && eastHand.containsCard(card))
				|| (currentPlayer.isSouth() && southHand.containsCard(card))
				|| (currentPlayer.isWest() && westHand.containsCard(card));
	}

	private Hand getHandOfCurrentPlayer() {
		if (this.currentPlayer.isNorth()) {
			return this.northHand;
		}
		if (this.currentPlayer.isEast()) {
			return this.eastHand;
		}
		if (this.currentPlayer.isSouth()) {
			return this.southHand;
		}
		if (this.currentPlayer.isWest()) {
			return this.westHand;
		}
		throw new RuntimeException("Invalid current player");
	}

	private void sortHands() {
		northHand.sort();
		eastHand.sort();
		southHand.sort();
		westHand.sort();
	}

}
