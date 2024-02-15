package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.COMPLETE_TRICK_NUMBER_OF_CARDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.sbk.sbking.core.comparators.RankComparator;
import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

public class Trick {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    @Deprecated
    @SuppressWarnings("unused")
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

    public Map<Card, Direction> getCardDirectionMap() {
        Map<Card, Direction> mapDirectionCard = new HashMap<Card, Direction>();
        for (Card card : this.cards) {
            mapDirectionCard.put(card, this.directionOfCard(card));
        }
        return mapDirectionCard;
    }

    public Suit getLeadSuit() {
        Card leadCard = this.getLeadCard();
        if (leadCard == null) {
            return null;
        }
        return leadCard.getSuit();
    }

    private Card getLeadCard() {
        if (this.cards.isEmpty()) {
            return null;
        }
        return this.cards.get(0);
    }

    public Direction getWinnerWithoutTrumpSuit() {
        Card card = this.highestCardOfSuit(this.getLeadSuit());
        return this.directionOfCard(card);
    }

    private Card highestCardOfSuit(Suit suit) {
        SortedSet<Card> sortedCardsOfSuit = new TreeSet<Card>(new RankComparator());
        for (Card card : this.getCards()) {
            if (card.getSuit() == suit) {
                sortedCardsOfSuit.add(card);
            }
        }
        return sortedCardsOfSuit.last();
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

    public boolean hasCardOf(Direction direction) {
        int stepsBetween = Direction.differenceBetween(this.getLeader(), direction);
        int minNumberOfCardsThatShouldBePlayedForDirectionToBeIncluded = stepsBetween + 1;
        int numberOfCardsInTable = this.getCards().size();
        return minNumberOfCardsThatShouldBePlayedForDirectionToBeIncluded <= numberOfCardsInTable;
    }

    public Map<Card, Direction> getCardsFromLastUpTo(Direction direction) {
        Map<Card, Direction> cardsUpToDirection = new HashMap<Card, Direction>();
        int directionPosition = Direction.differenceBetween(this.leader, direction);
        for (int i = this.cards.size() - 1; i >= directionPosition; i--) {
            Direction currentDirection = this.directionOfCard(this.cards.get(i));
            cardsUpToDirection.put(this.cards.get(i), currentDirection);
        }
        return cardsUpToDirection;
    }

    public void removeCardsFromLastUpTo(Direction direction) {
        int directionIndex = Direction.differenceBetween(this.getLeader(), direction);
        for (int i = this.cards.size() - 1; i >= directionIndex; i--) {
            this.cards.remove(i);
        }
    }

    public Direction getLastPlayer() {
        return this.leader.next(3);
    }

}
