package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.jelements.CardButton;
import br.com.sbk.sbking.gui.jelements.SitOrLeaveButton;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

public class HandElement {

    private DeckCardImageInformation deckCardImageInformation;
    private static final java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255, 0, 0);

    public HandElement(Deal deal, Container container, ActionListener actionListener, Point handCenter, Player player,
            boolean isVisible, Direction direction) {
        Hand hand = deal.getHandOf(direction);
        boolean isTurnToPlay = direction.equals(deal.getCurrentPlayer());

        this.deckCardImageInformation = new DeckCardImageInformation();

        int xOffset = (((hand.size() - 1) * deckCardImageInformation.getWidthBetweenCards())
                + deckCardImageInformation.getCardWidth()) / 2;
        xOffset *= -1;
        int yOffset = deckCardImageInformation.getCardHeight() / 2;
        yOffset *= -1;
        Point handTopLeftCorner = new Point(handCenter);
        handTopLeftCorner.translate(xOffset, yOffset);

        for (int i = hand.size() - 1; i >= 0; i--) { // This way, it draws correctly
            Card card = hand.get(i);
            CardButton cardButton = new CardButton(card, deckCardImageInformation);
            if (!isVisible) {
                cardButton.flip();
            }
            if (actionListener != null && isVisible) {
                cardButton.addActionListener(actionListener);
            }
            container.add(cardButton); // This line needs to go before setting the button location
            cardButton.setLocation(locationOfCard(i, handTopLeftCorner)); // This line needs to go after adding the
            // button to the container
            cardButton.setIsInHand(true);
        }

        JButton sitOrLeaveButton = new SitOrLeaveButton(direction);
        sitOrLeaveButton.addActionListener(actionListener);
        if (player == null) {
            sitOrLeaveButton.setText("Click to seat.");
        } else {
            sitOrLeaveButton.setText(player.getNickname());
        }

        if (isTurnToPlay) {
            sitOrLeaveButton.setBackground(TURN_LIGHT_COLOR);
            sitOrLeaveButton.setOpaque(true);
        }

        Point startingPoint = handTopLeftCorner;
        handTopLeftCorner.translate(0, deckCardImageInformation.getCardHeight() + 5);
        sitOrLeaveButton.setLocation(startingPoint);
        container.add(sitOrLeaveButton);
    }

    private Point locationOfCard(int index, Point handTopLeftCorner) {
        Point cardLocation = (Point) handTopLeftCorner.clone();
        cardLocation.translate(index * deckCardImageInformation.getWidthBetweenCards(), 0);
        return cardLocation;
    }

}
