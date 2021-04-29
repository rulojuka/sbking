package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import br.com.sbk.sbking.gui.jelements.LeaveTableButton;

public class LeaveTableElement {

  public LeaveTableElement(Container container, Point point, ActionListener actionListener) {
    JButton leaveTableButton = new LeaveTableButton();
    leaveTableButton.addActionListener(actionListener);
    leaveTableButton.setLocation(point);
    container.add(leaveTableButton);
  }

}
