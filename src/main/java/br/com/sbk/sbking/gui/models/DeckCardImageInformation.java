package br.com.sbk.sbking.gui.models;

import java.net.URL;

import java.awt.Image;

import javax.swing.ImageIcon;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.main.AssetLoader;


public class DeckCardImageInformation {
    private static final String DIRECTORY = "/images/cards/";

    private static final int ORIGINAL_CARD_IMAGE_WIDTH = 182;
    private static final int ORIGINAL_CARD_IMAGE_HEIGHT = 247;
    private static final int ORIGINAL_WIDTH_BETWEEN_CARDS = 48;

    private static double scaleFactor;
    private static int cardWidth;
    private static int cardHeight;
    private static int widthBetweenCards;

    public DeckCardImageInformation() {
        scaleFactor = FrameConstants.getScreenScale();
        cardWidth = (int) (ORIGINAL_CARD_IMAGE_WIDTH * scaleFactor);
        cardHeight = (int) (ORIGINAL_CARD_IMAGE_HEIGHT * scaleFactor);
        widthBetweenCards = (int) (ORIGINAL_WIDTH_BETWEEN_CARDS * scaleFactor);
    }

    private String getFilename(Suit suit, Rank rank) {
        return suit.getSymbol() + rank.getSymbol().toLowerCase() + ".png";
    }

    public ImageIcon createFrontImage(Card card) {
        String imagePath = DIRECTORY + getFilename(card.getSuit(), card.getRank());
        URL url = getClass().getResource(imagePath);
        return getScaledCardImage(new ImageIcon(url).getImage());
    }

    public ImageIcon createBackImage() {
        String imagePath = DIRECTORY + "back.png";
        URL url = getClass().getResource(imagePath);
        return getScaledCardImage(new ImageIcon(url).getImage());
    }

    private ImageIcon getScaledCardImage(Image img) {
        return new ImageIcon(img.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    public ImageIcon getBackImage() {
        return AssetLoader.getCachedBack();
    }

    public ImageIcon getFrontImage(Card card) {
        return AssetLoader.getCachedFrontImage(card);
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public int getWidthBetweenCards() {
        return widthBetweenCards;
    }
}
