package gui;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import core.Card;
import core.Deck;
import core.Rank;
import core.Suit;

public class DeckOfCardButtons {

	private final int NUMBER_OF_HANDS = 4;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
	private final int EAST_X = NORTH_X + 300;
	private final int WEST_X = NORTH_X - 300;
	private final int NORTH_Y = 120;
	private final int SOUTH_Y = 470;
	private final int EAST_Y = 300;
	private final int WEST_Y = EAST_Y;
	private final int CARD_WIDTH = 72;
	private final int CARD_HEIGHT = 96;

	private ImageIcon backImage;

	// Card and turn light positioning arrays

	public int[] initial_x = new int[4];
	public int[] initial_y = new int[4];

	private List<CardButton> listOfCardButtons = new ArrayList<CardButton>();
	// FIXME This next line should not have a hardcoded directory
	public String directory = "/home/rulojuka/workspace/sbking/data/images/cards/";

	public DeckOfCardButtons(Deck deck, ActionListener actionListener) {
		backImage = new ImageIcon(directory + "b1fv.png");
		setInitialHandPositioning();

		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			for (int i = SIZE_OF_HAND - 1; i >= 0; i--) {
				Card card = deck.dealCard();
				String imageFile = directory + getFilename(card.getSuit(), card.getRank());
				ImageIcon frontImage = new ImageIcon(imageFile);
				CardButton cardButton = new CardButton(card, frontImage, backImage, actionListener);
				cardButton.setBounds(initial_x[k] + i * BETWEEN_CARDS_WIDTH, initial_y[k], CARD_WIDTH, CARD_HEIGHT);
				this.listOfCardButtons.add(cardButton);
			}
		}
	}

	private String getFilename(Suit suit, Rank rank) {
		return suit.getSymbol() + rank.getSymbol() + ".png";
	}

	private void setInitialHandPositioning() {
		initial_y[0] = NORTH_Y;
		initial_y[1] = EAST_Y;
		initial_y[2] = SOUTH_Y;
		initial_y[3] = WEST_Y;
		initial_x[0] = NORTH_X;
		initial_x[1] = EAST_X;
		initial_x[2] = SOUTH_X;
		initial_x[3] = WEST_X;
	}

	public void addAllCardButtonsToPane(Container contentPane) {
		for (CardButton cardButton : listOfCardButtons) {
			contentPane.add(cardButton);
		}

	}

}