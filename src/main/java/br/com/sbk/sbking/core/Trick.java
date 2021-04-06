package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.COMPLETE_TRICK_NUMBER_OF_CARDS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

@SuppressWarnings("serial")
public class Trick implements Serializable {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    private Trick() {
    }

    private List<Card> cards;
    private Direction leader;
    private boolean lastTwo;

    public Trick(Direction leader) {
        this.leader = leader;
        this.cards = new ArrayList<Card>();
        this.lastTwo = false;
    }

    public void addCard(Card card) {
        if (!this.isComplete()) {
            this.cards.add(card);
        } else {
            throw new TrickAlreadyFullException();
        }
    }

    public boolean isComplete() {
        return this.getNumberOfCards() == COMPLETE_TRICK_NUMBER_OF_CARDS;
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public Direction getLeader() {
        return this.leader;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(this.cards);
    }

    public Suit getLeadSuit() {
        return this.getLeadCard().getSuit();
    }

    private Card getLeadCard() {
        return this.getCards().get(0);
    }

    public Direction getWinnerWithoutTrumpSuit() {
        Card card = this.highestCardOfSuit(this.getLeadSuit());
        return this.directionOfCard(card);
    }

    private Card highestCardOfSuit(Suit suit) {
        SortedSet<Card> sortedCardsOfSuit = new TreeSet<Card>(new CardOfSameSuitComparator());
        for (Card card : this.getCards()) {
            if (card.getSuit() == suit) {
                sortedCardsOfSuit.add(card);
            }
        }
        return sortedCardsOfSuit.last();
    }

    private static class CardOfSameSuitComparator implements Comparator<Card> {
        @Override
        public int compare(Card card1, Card card2) {
            return card1.compareRank(card2);
        }
    }

    private Direction directionOfCard(Card card) {
        Direction currentDirection = leader;
        Direction response = null;
        for (Card cardFromTrick : this.getCards()) {
            if (card.equals(cardFromTrick)) {
                response = currentDirection;
                break;
            } else {
                currentDirection = currentDirection.next();
            }
        }
        return response;
    }

    public Direction getWinnerWithTrumpSuit(Suit trumpSuit) {
        Card card;
        if (this.hasSuit(trumpSuit)) {
            card = this.highestCardOfSuit(trumpSuit);
        } else {
            card = this.highestCardOfSuit(this.getLeadSuit());
        }
        return this.directionOfCard(card);
    }

    private boolean hasSuit(Suit suit) {
        for (Card card : this.getCards()) {
            if (card.getSuit() == suit) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfMen() {
        int men = 0;
        for (Card c : this.getCards()) {
            if (c.isMan()) {
                men++;
            }
        }
        return men;
    }

    public int getNumberOfWomen() {
        int women = 0;
        for (Card c : this.getCards()) {
            if (c.isWoman()) {
                women++;
            }
        }
        return women;
    }

    public boolean isLastTwo() {
        return this.lastTwo;
    }

    public void setLastTwo() {
        this.lastTwo = true;
    }

    public boolean hasKingOfHearts() {
        for (Card c : this.getCards()) {
            if (c.isKingOfHearts()) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfHeartsCards() {
        int hearts = 0;
        for (Card c : this.getCards()) {
            if (c.isHeart()) {
                hearts++;
            }
        }
        return hearts;
    }

    private int getNumberOfCards() {
        return this.getCards().size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cards == null) ? 0 : cards.hashCode());
        result = prime * result + (lastTwo ? 1231 : 1237);
        result = prime * result + ((leader == null) ? 0 : leader.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Trick other = (Trick) obj;
        if (cards == null) {
            if (other.cards != null) {
                return false;
            }
        } else if (!cards.equals(other.cards)) {
            return false;
        }
        if (lastTwo != other.lastTwo) {
            return false;
        }
        if (leader != other.leader) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.cards.toString();
    }

}
