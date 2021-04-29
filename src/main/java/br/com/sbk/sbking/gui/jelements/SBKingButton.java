package br.com.sbk.sbking.gui.jelements;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SBKingButton extends JButton {

    public SBKingButton() {
        super();
        this.setContentAreaFilled(false); /* Paint always in the same order??? */

        this.setForeground(new java.awt.Color(255, 255, 255));
    }
}
