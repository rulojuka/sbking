package br.com.sbk.sbking.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class centralizes the access to Random.
 * This decouples other classes from Random but most importantly
 * increases the quality and efficiency of RNG.
 */
public class RandomUtils {
    private static SecureRandom random = new SecureRandom();

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public void shuffle(List<Card> listOfCards) {
        Collections.shuffle(listOfCards, random);
    }

    public Card pickOneRandomCard(Hand hand) {
        ArrayList<Card> cards = new ArrayList<Card>(hand.getCards());
        int randomIndex = this.nextInt(cards.size());
        return cards.get(randomIndex);
    }

}
