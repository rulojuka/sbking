package core;

import java.util.List;

import core.rulesets.Ruleset;

public class Board {

	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Trick currentTrick;
	private Direction currentPlayer;
	private int northSouthPoints;
	private int eastWestPoints;
	private Ruleset ruleset;

	public Board(List<Hand> hands, Ruleset ruleset) {
		this.northHand = hands.get(0);
		this.eastHand = hands.get(1);
		this.southHand = hands.get(2);
		this.westHand = hands.get(3);
		currentTrick = new Trick();
		currentPlayer = Direction.NORTH;
		northSouthPoints = 0;
		eastWestPoints = 0;
		this.ruleset = ruleset;
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

	public int getNorthSouthTricks() {
		return northSouthPoints;
	}

	public int getEastWestTricks() {
		return eastWestPoints;
	}

	private void sortHands() {
		northHand.sort();
		eastHand.sort();
		southHand.sort();
		westHand.sort();
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
		Suit leadSuit = trick.getSuit();

		if (hand.hasSuit(leadSuit) == false) {
			return true;
		} else if (card.getSuit() == leadSuit) {
			return true;
		}

		return false;
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
		if(currentTrick.isComplete()) {
			currentTrick.discard();
		}
		if (!followsSuit(card, getHandOfCurrentPlayer())) {
			throw new RuntimeException("Card does not follow suit.");
		}
		if (currentTrick.isEmpty()) {
			currentTrick.setLeader(currentPlayer);
		}

		currentTrick.addCard(card);
		getHandOfCurrentPlayer().removeCard(card);

		if (currentTrick.isComplete()) {
			Direction winner = currentTrick.getWinner();
			updatePoints();
			currentPlayer = winner;
		} else {
			currentPlayer = currentPlayer.next();
		}

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
	
	public Ruleset getRuleset() {
		return this.ruleset;
	}

}
