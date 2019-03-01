package gui;

import javax.swing.ImageIcon;

import core.Card;
import core.Rank;
import core.Suit;

public final class DeckCardImageInformation {
	
	private DeckCardImageInformation() {
		// This is here to prevent instantiation of this class. Yes, static classes suck, I know.
	}
	
	// FIXME This next line should not have a hardcoded directory
	public static final String directory = "/home/rulojuka/workspace/sbking/data/images/cards/";
	
	public static ImageIcon getBackImage() {
		return new ImageIcon(directory + "b1fv.png");
	}
	
	public static ImageIcon getFrontImage(Card card) {
		String imageFile = directory + getFilename(card.getSuit(), card.getRank());
		return new ImageIcon(imageFile);
	}
	
	private static String getFilename(Suit suit, Rank rank) {
		return suit.getSymbol() + rank.getSymbol() + ".png";
	}

}
