package br.com.sbk.sbking.gui;

import java.net.URL;

import javax.swing.ImageIcon;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class DeckCardImageInformation {

	private final String directory = "/images/cards/";

	public ImageIcon getBackImage() {
		String imagePath = directory + "b1fv.png";
		URL url = getClass().getResource(imagePath);
		return new ImageIcon(url);
	}

	public ImageIcon getFrontImage(Card card) {
		String imagePath = directory + getFilename(card.getSuit(), card.getRank());
		URL url = getClass().getResource(imagePath);
		return new ImageIcon(url);
	}

	private String getFilename(Suit suit, Rank rank) {
		return suit.getSymbol() + rank.getSymbol().toLowerCase() + ".png";
	}

}
