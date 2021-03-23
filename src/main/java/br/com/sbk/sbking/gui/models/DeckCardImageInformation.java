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

    private static double SCALE_FACTOR;
    private static int CARD_WIDTH;
    private static int CARD_HEIGHT;
    private static int WIDTH_BETWEEN_CARDS;

    public DeckCardImageInformation() {
        SCALE_FACTOR = FrameConstants.getScreenScale();
        CARD_WIDTH = (int) (ORIGINAL_CARD_IMAGE_WIDTH * SCALE_FACTOR);
        CARD_HEIGHT = (int) (ORIGINAL_CARD_IMAGE_HEIGHT * SCALE_FACTOR);
        WIDTH_BETWEEN_CARDS = (int) (ORIGINAL_WIDTH_BETWEEN_CARDS * SCALE_FACTOR);
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
        return new ImageIcon(img.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, java.awt.Image.SCALE_SMOOTH));
    }

    public ImageIcon getBackImage() {
        return AssetLoader.getCachedBack();
    }

    public ImageIcon getFrontImage(Card card) {
        return AssetLoader.getCachedFrontImage(card);
    }

    public int getCardWidth() {
        return CARD_WIDTH;
    }

    public int getCardHeight() {
        return CARD_HEIGHT;
    }

    public int getWidthBetweenCards() {
        return WIDTH_BETWEEN_CARDS;
    }
}
