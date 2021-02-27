package br.com.sbk.sbking.gui.JElements;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SBKingButton extends JButton {

	public SBKingButton() {
		super();
		// this.setFocusPainted(false);
		// this.setRolloverEnabled(false);
		this.setContentAreaFilled(false); /* Paint always in the same order??? */
		// this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		this.setForeground(new java.awt.Color(255, 255, 255));

	}

}
