package br.com.sbk.sbking.gui.jelements;

import java.awt.Point;

import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

@SuppressWarnings("serial")
public class CardButton extends SBKingButton {

    private ImageIcon frontImage;
    private ImageIcon backImage;
    private boolean faceUp;
    private Card card;
    private int offsetWhenSelected;
    private boolean isCardInHand;

    public CardButton(Card card, DeckCardImageInformation deckCardImageInformation) {
        super();

        this.card = card;
        this.frontImage = deckCardImageInformation.getFrontImage(card);
        this.backImage = deckCardImageInformation.getBackImage();

        offsetWhenSelected = deckCardImageInformation.getCardHeight() / 10;

        this.setSize(deckCardImageInformation.getCardWidth(), deckCardImageInformation.getCardHeight());
        this.faceUp = true;
        this.setIcon(frontImage);
        this.setBorderPainted(false);

        this.setMouseListener(this);
    }

    private void setCardAsSelected() {
        if (faceUp && isCardInHand) {
            this.translate(0, -offsetWhenSelected);
        }
    }

    private void setCardBackInHand() {
        if (faceUp && isCardInHand) {
            this.translate(0, offsetWhenSelected);
        }
    }

    private void translate(int offsetX, int offsetY) {
        Point newLocation = (Point) this.getLocation().clone();
        newLocation.translate(offsetX, offsetY);
        this.setLocation(newLocation);
    }

    private void setMouseListener(CardButton sbkingButton) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCardAsSelected();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCardBackInHand();
            }
        });
    }

    public void flip() {
        if (faceUp) {
            this.setIcon(this.backImage);
            faceUp = false;
        } else {
            this.setIcon(this.frontImage);
            faceUp = true;
        }
    }

    public Card getCard() {
        return card;
    }

    public void setIsInHand(boolean isCardInHand) {
        this.isCardInHand = isCardInHand;
    }
}
