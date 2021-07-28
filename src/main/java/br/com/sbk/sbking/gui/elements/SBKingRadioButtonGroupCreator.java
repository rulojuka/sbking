package br.com.sbk.sbking.gui.elements;

import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.gui.models.TextWithColor;

public class SBKingRadioButtonGroupCreator {

  private static int elementHeight = 20;

  public ButtonGroup create(List<TextWithColor> texts, int x, int y, int elementWidth) {
    ButtonGroup buttonGroup = new ButtonGroup();

    for (TextWithColor textWithColor : texts) {
      String text = textWithColor.getText();
      JRadioButton currentButton = new JRadioButton(text);
      currentButton.setName(text);
      currentButton.setBounds(x, y, elementWidth, elementHeight);
      currentButton.setForeground(textWithColor.getColor());
      y += elementHeight;

      buttonGroup.add(currentButton);
    }
    return buttonGroup;

  }

}
