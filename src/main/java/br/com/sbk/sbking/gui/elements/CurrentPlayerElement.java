package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;

public class CurrentPlayerElement {

	private static final int X_OFFSET = 0;
	private static final int Y_OFFSET = -130;
	private java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255, 0, 0);

	public CurrentPlayerElement(Direction currentPlayer, Container container, boolean isMyTurn) {

		int width = 110;
		int height = 15;

		Point tableCenter = new Point(HALF_WIDTH, HALF_HEIGHT);
		Point currentPlayerPosition = new Point(tableCenter);
		currentPlayerPosition.translate(X_OFFSET - width / 2, Y_OFFSET);
		
		String text;
		
		if(isMyTurn) {
			text = "It is your turn.";
		}else {
			text = currentPlayer.getCompleteName() + " to play.";
		}

		JLabel playerButton = new JLabel(text);
		playerButton.setHorizontalAlignment(CENTER);
		playerButton.setSize(width, height);
		playerButton.setLocation(currentPlayerPosition);
		playerButton.setBackground(TURN_LIGHT_COLOR);
		playerButton.setOpaque(true);
		container.add(playerButton);
	}

}
