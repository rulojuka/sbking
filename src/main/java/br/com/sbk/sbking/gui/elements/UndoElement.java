package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import br.com.sbk.sbking.gui.JElements.UndoButton;

public class UndoElement {

    public UndoElement(Container container, Point point, ActionListener actionListener) {

        JButton undoButton = new UndoButton();
        undoButton.addActionListener(actionListener);
        undoButton.setText("UNDO");
        undoButton.setBounds(point.x, point.y, 100, 30);
        container.add(undoButton);
    }

}
