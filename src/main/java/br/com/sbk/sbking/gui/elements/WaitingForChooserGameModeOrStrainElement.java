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

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;

public class WaitingForChooserGameModeOrStrainElement {

	public static void add(Container container, Direction direction) {
		JLabel waitingLabel = new JLabel(
				"<html>Waiting for " + direction.toString() + " to choose<br/>Game Mode or Strain.</html>");
		waitingLabel.setHorizontalAlignment(CENTER);
		int width = 300;
		int height = 30;
		int x;
		int y;
		if (Direction.NORTH == direction) {
			x = NORTH_X_CENTER;
			y = NORTH_Y_CENTER;
		} else if (Direction.EAST == direction) {
			x = EAST_X_CENTER;
			y = EAST_Y_CENTER;
		} else if (Direction.SOUTH == direction) {
			x = SOUTH_X_CENTER;
			y = SOUTH_Y_CENTER + 100;
		} else {
			x = WEST_X_CENTER;
			y = WEST_Y_CENTER;
		}
		waitingLabel.setSize(width, height);
		waitingLabel.setLocation(x, y);
		// rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(waitingLabel);
	}
}
