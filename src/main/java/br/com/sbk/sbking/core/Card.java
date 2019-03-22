package br.com.sbk.sbking.core;

public class Card {

	private Suit suit;
	private Rank rank;

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public Suit getSuit() {
		return this.suit;
	}

	public Rank getRank() {
		return this.rank;
	}

	public int compareRank(Card otherCard) {
		return this.getRank().compareTo(otherCard.getRank());
	}

	public int compareSuit(Card otherCard) {
		return this.getSuit().compareTo(otherCard.getSuit());
	}

	public boolean isMan() {
		return this.isJack() || this.isKing();
	}

	public boolean isWoman() {
		return this.getRank() == Rank.QUEEN;
	}

	public boolean isKingOfHearts() {
		return this.isKing() && this.isHeart();
	}

	private boolean isJack() {
		return this.getRank() == Rank.JACK;
	}

	private boolean isKing() {
		return this.getRank() == Rank.KING;
	}

	public boolean isHeart() {
		return this.getSuit() == Suit.HEARTS;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (this.getRank() != other.getRank())
			return false;
		if (this.getSuit() != other.getSuit())
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}
	
}
