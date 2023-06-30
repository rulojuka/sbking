package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;

public class Board {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Board() {
    }

    private Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
    private Direction dealer;

    public Board(Map<Direction, Hand> hands, Direction dealer) {
        this.hands = hands;

        this.sortAllHands(new CardInsideHandComparator());
        this.dealer = dealer;
    }

    public Direction getDealer() {
        return dealer;
    }

    public Hand getHandOf(Direction direction) {
        return this.hands.get(direction);
    }

    public void sortAllHands(Comparator<Card> comparator) {
        for (Direction direction : Direction.values()) {
            this.getHandOf(direction).sort(comparator);
        }
    }

    public List<Card> removeOneCardFromEachHand() {
        List<Card> removedCards = new ArrayList<Card>();
        for (Direction direction : Direction.values()) {
            Hand currentHand = this.getHandOf(direction);
            Card removedCard = currentHand.removeOneRandomCard();
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

    public void putCardInHand(Map<Card, Direction> cardDirectionMap) {
        for (Map.Entry<Card, Direction> cardDirection : cardDirectionMap.entrySet()) {
            this.hands.get(cardDirection.getValue()).addCard(cardDirection.getKey());
        }
    }

}
