package br.com.sbk.sbking.core;

public class Card {

	private Suit suitValue;
	private Rank rankValue;

	public Card(Suit suit, Rank rank) {
		suitValue = suit;
		rankValue = rank;
	}

	public Suit getSuit() {
		return suitValue;
	}

	public Rank getRank() {
		return rankValue;
	}

	public String toString() {
		return rankValue.getName() + " of " + suitValue.getName();
	}

	public int compareRank(Card otherCard) {
		return this.getRank().compareTo(otherCard.getRank());
	}

	public int compareSuit(Card otherCard) {
		return this.getSuit().compareTo(otherCard.getSuit());
	}

	public boolean isMan() {
		return isJack() || isKing();
	}

	public boolean isWoman() {
		return this.rankValue == Rank.QUEEN;
	}

	public boolean isHeart() {
		return this.suitValue == Suit.HEARTS;
	}

	public int points() {
		int points = 0;
		if (this.getRank().compareTo(Rank.ACE) == 0)
			points = 4;
		if (this.getRank().compareTo(Rank.KING) == 0)
			points = 3;
		if (this.getRank().compareTo(Rank.QUEEN) == 0)
			points = 2;
		if (this.getRank().compareTo(Rank.JACK) == 0)
			points = 1;
		return points;
	}

	public boolean isKingOfHearts() {
		return isKing() && isHeart();
	}

	private boolean isKing() {
		return this.rankValue == Rank.KING;
	}

	private boolean isJack() {
		return this.rankValue == Rank.JACK;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (rankValue != other.getRank())
			return false;
		if (suitValue != other.getSuit())
			return false;
		return true;
	}
	


}
