package br.com.sbk.sbking.gui.elements;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.gui.elements.ChooseGameModeOrStrainElement.GameModeOrStrainSelectActionListener;

public class SBKingRadioButtonGroupCreator {

  private static int elementWidth = 160;
  private static int elementHeight = 20;
  private static int xSpacing = 5;
  private static int BUTTON_WIDTH = 80;

  public ButtonGroup create(List<String> texts, int x, int y) {
    ButtonGroup buttonGroup = new ButtonGroup();

    List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();
    for (String text : texts) {
      JRadioButton currentButton = new JRadioButton(text);
      currentButton.setBounds(x, y, elementWidth, elementHeight);
      y += elementHeight;

      // container.add(currentButton);
      radioButtons.add(currentButton);
      buttonGroup.add(currentButton);
    }
    return buttonGroup;

    // JButton selectButton = new JButton();
    // Point selectButtonPosition = new Point(x, y);
    // selectButtonPosition.translate(elementWidth + xSpacing, 0);
    // selectButton.addActionListener(new GameModeOrStrainSelectActionListener());
    // selectButton.setLocation(selectButtonPosition);
    // selectButton.setSize(BUTTON_WIDTH, elementHeight * texts.size());
    // selectButton.setText("Select");
    // container.add(selectButton);
  }

}
