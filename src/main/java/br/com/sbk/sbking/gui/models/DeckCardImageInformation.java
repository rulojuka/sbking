package br.com.sbk.sbking.gui.models;

import java.net.URL;

import java.awt.Image;

import javax.swing.ImageIcon;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class DeckCardImageInformation {
	private static final String directory = "/images/cards/";
	private static final int CARD_IMAGE_WIDTH = 72;
	private static final int CARD_IMAGE_HEIGHT = 96; 
	private static final int SCALE_FACTOR = 2;

	private static final int CARD_WIDTH = CARD_IMAGE_HEIGHT*SCALE_FACTOR;
	private static final int CARD_HEIGHT = CARD_IMAGE_HEIGHT*SCALE_FACTOR;

	public ImageIcon getBackImage() {
		String imagePath = directory + "b1fv.png";
		URL url = getClass().getResource(imagePath);

		return getScaledCardImage(url);
	}

	public ImageIcon getFrontImage(Card card) {
		String imagePath = directory + getFilename(card.getSuit(), card.getRank());
		URL url = getClass().getResource(imagePath);
		return getScaledCardImage(url);
	}

	private String getFilename(Suit suit, Rank rank) {
		return suit.getSymbol() + rank.getSymbol().toLowerCase() + ".png";
	}

	public int getCardWidth() {
		return CARD_WIDTH;
	}

	public int getCardHeight() {
		return CARD_HEIGHT;
	}

	private ImageIcon getScaledCardImage(URL url) {
		Image cardImage = new ImageIcon(url).getImage();
		return new ImageIcon(cardImage.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, java.awt.Image.SCALE_SMOOTH));
	}
}
