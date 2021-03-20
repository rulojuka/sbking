package br.com.sbk.sbking.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Card implements Serializable {

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

    @Override
    public String toString() {
        return this.suit.getSymbol() + this.rank.getSymbol();
    }

    public int getPoints() {
        Map<Rank, Integer> points = new HashMap<Rank, Integer>();
        points.put(Rank.ACE, 4);
        points.put(Rank.KING, 3);
        points.put(Rank.QUEEN, 2);
        points.put(Rank.JACK, 1);

        Integer value = points.get(this.rank);
        if (value == null) {
            return 0;
        } else {
            return value;
        }
    }

}
