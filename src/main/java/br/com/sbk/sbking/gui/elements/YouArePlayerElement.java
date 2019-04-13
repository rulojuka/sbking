package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_Y_CENTER;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;

public class YouArePlayerElement {

	public static void add(Direction direction, Container container) {
		JLabel playerLabel = new JLabel("You are " + direction.getCompleteName());
		playerLabel.setHorizontalAlignment(CENTER);
		playerLabel.setSize(100, 15);
		playerLabel.setLocation(locationOf(direction));
		playerLabel.setOpaque(true);
		container.add(playerLabel);
	}

	private static Point locationOf(Direction direction) {
		int x = 0;
		int y = 0;
		if (Direction.NORTH == direction) {
			x = NORTH_X_CENTER;
			y = NORTH_Y_CENTER;
		}
		if (Direction.EAST == direction) {
			x = EAST_X_CENTER;
			y = EAST_Y_CENTER;
		}
		if (Direction.SOUTH == direction) {
			x = SOUTH_X_CENTER;
			y = SOUTH_Y_CENTER + 100;
		}
		if (Direction.WEST == direction) {
			x = WEST_X_CENTER;
			y = WEST_Y_CENTER;
		}
		return new Point(x, y);
	}

}
