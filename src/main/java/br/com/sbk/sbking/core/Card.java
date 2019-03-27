package br.com.sbk.sbking.core;

import java.io.Serializable;

import br.com.sbk.sbking.core.exceptions.IllegalCardNameException;

@SuppressWarnings("serial")
public class Card implements Serializable {

	private Suit suit;
	private Rank rank;

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	/**
	 * 
	 * @param spaceSeparatedName Name of the card in the format
	 *                           "<suit><space><rank>" Examples: "Clubs Two",
	 *                           "Diamonds King"
	 */
	public Card(String spaceSeparatedName) {
		String[] split = spaceSeparatedName.split(" ");
		String suitString = split[0];
		String rankString = split[1];
		Suit suit = null;
		Rank rank = null;
		for (Suit currentSuit : Suit.values()) {
			if (currentSuit.getName().equals(suitString)) {
				suit = currentSuit;
			}
		}
		for (Rank currentRank : Rank.values()) {
			if (currentRank.getName().equals(rankString)) {
				rank = currentRank;
			}
		}
		if (suit == null || rank == null) {
			throw new IllegalCardNameException();
		}
		this.rank = rank;
		this.suit = suit;
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

	@Override
	public String toString() {
		return this.suit.getSymbol() + this.rank.getSymbol();
	}

	public String completeName() {
		return this.suit.getName() + this.rank.getName();
	}

}
