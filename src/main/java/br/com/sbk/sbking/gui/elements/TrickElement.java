package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

public class TrickElement {

    private List<CardButton> trick;
    private DeckCardImageInformation deckCardImageInformation;

    public TrickElement(Trick currentTrick, Container container, Point point) {
        deckCardImageInformation = new DeckCardImageInformation();
        trick = new ArrayList<CardButton>();
        Direction leader = currentTrick.getLeader();
        List<Card> listOfCards = currentTrick.getCards();
        for (int i = listOfCards.size() - 1; i >= 0; i--) {
            Card card = listOfCards.get(i);
            CardButton cardButton = new CardButton(card, deckCardImageInformation);
            trick.add(cardButton);
            container.add(cardButton); // This line needs to go before setting the button location
            cardButton.setLocation(cardLocation(leader.next(i), point)); // This line needs to go after adding the
            // button to the container
            cardButton.setIsInHand(false);
        }
    }

    private Point cardLocation(Direction direction, Point center) {
        int offsetY = deckCardImageInformation.getCardHeight() / 5;
        int x = center.x - deckCardImageInformation.getCardWidth() / 2;
        int y = center.y - deckCardImageInformation.getCardHeight() / 2;
        int xDiff = deckCardImageInformation.getWidthBetweenCards();
        int yDiff = (int) (deckCardImageInformation.getWidthBetweenCards() * 1.4);
        int dx;
        int dy;

        if (direction == Direction.NORTH) {
            dx = 0;
            dy = -yDiff;
        } else if (direction == Direction.EAST) {
            dx = +xDiff;
            dy = 0;
        } else if (direction == Direction.SOUTH) {
            dx = 0;
            dy = +yDiff;
        } else {
            dx = -xDiff;
            dy = 0;
        }
        return new Point(x + dx, y + dy + offsetY);
    }

}
