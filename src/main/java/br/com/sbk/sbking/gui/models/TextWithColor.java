package br.com.sbk.sbking.gui.models;

import java.awt.Color;

public class TextWithColor {

  private java.awt.Color color;
  private String text;

  public TextWithColor(Color color, String text) {
    this.color = color;
    this.text = text;
  }

  public TextWithColor(String text) {
    this.color = new Color(0, 0, 0);
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public Color getColor() {
    return color;
  }

}
