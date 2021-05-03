package br.com.sbk.sbking.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
        int numberOfCards = cards.size();
        int lastCardIndex = numberOfCards - 1;
        Card removedCard = null;

        Random random = new SecureRandom();

        int removedCardIndex = random.nextInt(numberOfCards);
        if (numberOfCards > 0) {
            Card lastCard = this.cards.get(lastCardIndex);
            removedCard = this.cards.get(removedCardIndex);
            cards.set(removedCardIndex, lastCard);
            cards.remove(lastCardIndex);
        }
        return removedCard;
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
