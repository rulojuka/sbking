package br.com.sbk.sbking.gui.JElements;

import java.awt.Font;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SBKingLabel extends JLabel {

  public SBKingLabel(String text) {
    super(text);
    this.setFont(new Font("Verdana", Font.PLAIN, 14));
    this.setForeground(new java.awt.Color(255, 255, 255));
  }
}
