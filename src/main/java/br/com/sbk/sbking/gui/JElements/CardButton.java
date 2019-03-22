package br.com.sbk.sbking.gui.JElements;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import br.com.sbk.sbking.core.Card;

@SuppressWarnings("serial")
public class CardButton extends JButton {

	private ImageIcon frontImage;
	private ImageIcon backImage;
	private boolean faceUp;
	private Card card;
	private static final int CARD_WIDTH = 72;
	private static final int CARD_HEIGHT = 96;

	public CardButton(Card card, ImageIcon frontImage, ImageIcon backImage) {
		super();

		this.card = card;
		this.frontImage = frontImage;
		this.backImage = backImage;

		this.setSize(CARD_WIDTH, CARD_HEIGHT);
		this.faceUp = true;
		this.setIcon(frontImage);

		this.setFocusPainted(false);
		this.setRolloverEnabled(false);
		this.setContentAreaFilled(false); /* Paint always in the same order??? */
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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
