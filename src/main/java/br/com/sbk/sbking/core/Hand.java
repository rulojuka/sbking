package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    public Collection<Card> getCards() {
        return Collections.unmodifiableCollection(this.cards);
    }

    public HandEvaluations getHandEvaluations() {
        return new HandEvaluations(this);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public Card removeOneRandomCard() {
        Card randomCardFromHand = new RandomUtils().pickOneRandomCard(this);
        this.removeCard(randomCardFromHand);
        return randomCardFromHand;
    }

    public Card get(int position) {
        return this.getCardsAsList().get(position);
    }

    public int size() {
        return this.getCardsAsList().size();
    }

    public void sort(Comparator<Card> comparator) {
        Collections.sort(this.cards, comparator);
    }

    public boolean containsCard(Card card) {
        return this.getCardsAsList().contains(card);
    }

    public boolean hasSuit(Suit suit) {
        for (Card card : this.getCardsAsList()) {
            if (card.getSuit() == suit) {
                return true;
            }
        }
        return false;
    }

    private List<Card> getCardsAsList() {
        return Collections.unmodifiableList(this.cards);
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("|");
        for (Card card : cards) {
            response.append(card.toString());
            response.append("|");
        }
        return response.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cards == null) ? 0 : cards.hashCode());
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
        Hand other = (Hand) obj;
        if (cards == null) {
            if (other.cards != null) {
                return false;
            }
        } else if (!cards.equals(other.cards)) {
            return false;
        }
        return true;
    }

}
