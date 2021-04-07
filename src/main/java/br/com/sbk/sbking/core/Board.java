package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    private Board() {
    }

    private Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    private Direction dealer;

    public Board(Map hands, Direction dealer) {
        this.hands = hands;

        this.sortAllHands();
        this.dealer = dealer;
    }

    public Direction getDealer() {
        return dealer;
    }

    public Hand getHandOf(Direction direction) {
        return this.hands.get(direction);
    }

    private void sortAllHands() {
        for (Direction direction : Direction.values()) {
            this.getHandOf(direction).sort();
        }
    }

    public void sortAllHandsByTrumpSuit(Suit suit) {
        for (Direction direction : Direction.values()) {
            this.getHandOf(direction).sortByTrumpSuit(suit);
        }
    }

    public List<Card> removeOneCardFromEachHand() {
        List<Card> removedCards = new ArrayList<Card>();
        for (Direction direction : Direction.values()) {
            Card removedCard = this.getHandOf(direction).removeOneRandomCard();
            removedCards.add(removedCard);
        }
        return removedCards;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dealer == null) ? 0 : dealer.hashCode());
        result = prime * result + ((hands == null) ? 0 : hands.hashCode());
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
        Board other = (Board) obj;
        if (dealer != other.dealer) {
            return false;
        }
        if (hands == null) {
            if (other.hands != null) {
                return false;
            }
        } else if (!hands.equals(other.hands)) {
            return false;
        }
        return true;
    }

}
