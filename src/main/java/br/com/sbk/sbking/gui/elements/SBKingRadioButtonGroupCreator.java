package br.com.sbk.sbking.gui.elements;

import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.gui.models.TextWithColorAndFont;

public class SBKingRadioButtonGroupCreator {

  private static int elementHeight = 20;

  public ButtonGroup create(List<TextWithColorAndFont> texts, int x, int y, int elementWidth) {
    ButtonGroup buttonGroup = new ButtonGroup();

    for (TextWithColorAndFont textWithColorAndFont : texts) {
      String text = textWithColorAndFont.getText();
      JRadioButton currentButton = new JRadioButton(text);
      currentButton.setName(text);
      currentButton.setBounds(x, y, elementWidth, elementHeight);
      currentButton.setForeground(textWithColorAndFont.getColor());
      currentButton.setFont(textWithColorAndFont.getFont());
      currentButton.setToolTipText(textWithColorAndFont.getCompleteLabel());
      y += elementHeight;

      buttonGroup.add(currentButton);
    }
    return buttonGroup;

  }

}
