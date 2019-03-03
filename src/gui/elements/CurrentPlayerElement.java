package gui.elements;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;
import static javax.swing.SwingConstants.CENTER;

import core.Direction;

public class CurrentPlayerElement {

	private java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255, 0, 0);

	public CurrentPlayerElement(Direction currentPlayer, Container container, Point location) {

		JLabel playerButton = new JLabel(currentPlayer.getCompleteName() + " to play.");
		playerButton.setHorizontalAlignment(CENTER);
		playerButton.setSize(100, 15);
		playerButton.setLocation(location);
		playerButton.setBackground(TURN_LIGHT_COLOR);
		playerButton.setOpaque(true);
		container.add(playerButton);
	}

}
