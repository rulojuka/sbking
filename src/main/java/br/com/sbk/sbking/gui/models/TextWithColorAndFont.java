package br.com.sbk.sbking.gui.models;

import java.awt.Color;
import java.awt.Font;

public class TextWithColorAndFont {

  private java.awt.Color color;
  private String text;
  private Font font;

  public TextWithColorAndFont(String text, Color color, int size) {
    this.color = color;
    this.text = text;
    this.font = new Font("Verdana", Font.PLAIN, size);
  }

  public TextWithColorAndFont(String text) {
    this.color = new Color(0, 0, 0);
    this.text = text;
    this.font = new Font("Verdana", Font.BOLD, 14);
  }

  public String getText() {
    return text;
  }

  public Color getColor() {
    return color;
  }

  public Font getFont() {
    return this.font;
  }

}
