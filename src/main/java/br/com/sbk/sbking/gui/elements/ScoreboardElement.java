package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JButton;

import br.com.sbk.sbking.core.Deal;

public class ScoreboardElement {

    public ScoreboardElement(Deal board, Container container, Point point) {

        JButton scoreboardButton = new JButton();
        scoreboardButton.setText("NS:" + board.getNorthSouthPoints() + " EW:" + board.getEastWestPoints());
        scoreboardButton.setBounds(point.x, point.y, 120, 120);
        container.add(scoreboardButton);
    }

}
