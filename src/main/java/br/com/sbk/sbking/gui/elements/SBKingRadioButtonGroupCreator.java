package br.com.sbk.sbking.gui.elements;

import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class SBKingRadioButtonGroupCreator {

  private static int elementWidth = 160;
  private static int elementHeight = 20;

  public ButtonGroup create(List<String> texts, int x, int y) {
    ButtonGroup buttonGroup = new ButtonGroup();

    for (String text : texts) {
      JRadioButton currentButton = new JRadioButton(text);
      currentButton.setName(text);
      currentButton.setBounds(x, y, elementWidth, elementHeight);
      y += elementHeight;

      buttonGroup.add(currentButton);
    }
    return buttonGroup;

  }

}
