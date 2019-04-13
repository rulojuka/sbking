package br.com.sbk.sbking.gui.JElements;

import javax.swing.ImageIcon;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

@SuppressWarnings("serial")
public class CardButton extends SBKingButton {

	private ImageIcon frontImage;
	private ImageIcon backImage;
	private boolean faceUp;
	private Card card;

	public CardButton(Card card, DeckCardImageInformation deckCardImageInformation) {
		super();

		this.card = card;
		this.frontImage = deckCardImageInformation.getFrontImage(card);
		this.backImage = deckCardImageInformation.getBackImage();

		this.setSize(deckCardImageInformation.getCardWidth(), deckCardImageInformation.getCardHeight());
		this.faceUp = true;
		this.setIcon(frontImage);
	}

	public void flip() {
		if (this.faceUp) {
			this.setIcon(this.backImage);
			this.faceUp = false;
		} else {
			this.setIcon(this.frontImage);
			this.faceUp = true;
		}
	}

	public Card getCard() {
		return card;
	}

}
