package core;

public class Board {

	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Trick currentTrick;
	private Direction currentPlayer;
	private int northSouthTricks;
	private int eastWestTricks;

	public Board(Hand northHand, Hand eastHand, Hand southHand, Hand westHand) {
		this.northHand = northHand;
		this.eastHand = eastHand;
		this.southHand = southHand;
		this.westHand = westHand;
		currentTrick = new Trick();
		currentPlayer = Direction.NORTH;
		northSouthTricks = 0;
		eastWestTricks = 0;
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
		return northSouthTricks;
	}

	public int getEastWestTricks() {
		return eastWestTricks;
	}

	private void sortHands() {
		northHand.sort();
		eastHand.sort();
		southHand.sort();
		westHand.sort();
	}

	/**
	 * 
	 * @param c Card that is going to be be validated
	 * @param h Hand of the player that is playing that card
	 * @return True if the card that is being played follow the basic trick rules.
	 *         False if it does not.
	 */
	public boolean isValid(Card c, Hand h) {
		Trick myTrick = this.getCurrentTrick();
		if (myTrick.getNumberOfCards() == 0)
			return true;
		Suit leadSuit = myTrick.getCard(0).getSuit();

		if (h.hasSuit(leadSuit) == false)
			return true;
		if (h.hasSuit(leadSuit) == true && c.getSuit() == leadSuit)
			return true;

		return false;
	}

	/**
	 * This method will see if playing the card is a valid move. If it is, it will
	 * play it.
	 * 
	 * @param card Card to be played on the board.
	 */
	public void playCard(Card card) {
		if (playedCardIsFromCurrentPlayer(card)) {
			currentTrick.addCard(card);

			if (currentTrick.isComplete()) {
				Direction winner = currentTrick.winner();
				currentTrick.discard();
				currentTrick.setLeader(winner);
				currentPlayer = winner;
				updateScoreboard();
			} else {
				currentPlayer = currentPlayer.next();
			}
		} else {
			throw new RuntimeException("Trying to play card in other player turn");
		}
	}

	private void updateScoreboard() {
		if (currentTrick.isComplete()) {
			Direction winner = currentTrick.winner();
			if (winner.isNorthSouth()) {
				northSouthTricks++;
			} else {
				eastWestTricks++;
			}
		}
	}

	private boolean playedCardIsFromCurrentPlayer(Card card) {
		return (currentPlayer.isNorth() && northHand.containsCard(card))
				|| (currentPlayer.isEast() && eastHand.containsCard(card))
				|| (currentPlayer.isSouth() && southHand.containsCard(card))
				|| (currentPlayer.isWest() && westHand.containsCard(card));
	}

	public Hand getHandOfCurrentPlayer() {
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

}
