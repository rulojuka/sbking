package br.com.sbk.sbking.gui.models;

import java.awt.Color;
import java.awt.Font;

import br.com.sbk.sbking.gui.constants.FontConstants;

public class TextWithColorAndFont {

  private java.awt.Color color;
  private String text;
  private Font font;
  private String toolTipText;

  public TextWithColorAndFont(String text, Color color, int size, String toolTipText) {
    this.color = color;
    this.text = text;
    this.font = new Font(FontConstants.FONT_NAME, Font.PLAIN, size);
    this.toolTipText = toolTipText;
  }

  public TextWithColorAndFont(String text, String toolTipText) {
    this(text, new Color(0, 0, 0), 14, toolTipText);
  }

  public TextWithColorAndFont(String text) {
    this(text, new Color(0, 0, 0), 14, "");
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

  public String getCompleteLabel() {
    return this.toolTipText;
  }

}
