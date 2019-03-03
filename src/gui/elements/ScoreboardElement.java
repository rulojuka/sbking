package gui.elements;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JButton;

import core.Board;

public class ScoreboardElement {

	public ScoreboardElement(Board board, Container container, Point point) {
		
		JButton scoreboardButton = new JButton();
		scoreboardButton.setText("NS:" + board.getNorthSouthPoints() + " EW:" + board.getEastWestPoints());
		scoreboardButton.setBounds(point.x,point.y,120,120);
		container.add(scoreboardButton);
	}

}
