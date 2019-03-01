package gui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import core.Card;

@SuppressWarnings("serial")
public class CardButton extends JButton {

	private ImageIcon frontImage;
	private ImageIcon backImage;
	private boolean faceUp;

	public CardButton(Card card, ImageIcon frontImage, ImageIcon backImage, ActionListener actionListener) {
		super();

		this.frontImage = frontImage;
		this.backImage = backImage;

		this.faceUp = true;
		this.setIcon(frontImage);

		this.setRolloverEnabled(false);
		this.setContentAreaFilled(false); /* Paint always in the same order??? */
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		this.addActionListener(actionListener);
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

}
