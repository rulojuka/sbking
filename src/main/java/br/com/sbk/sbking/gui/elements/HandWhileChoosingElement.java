package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_Y_CENTER;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;

public class HandWhileChoosingElement {

	public HandWhileChoosingElement(Container contentPane, Board board, Direction direction) {
		Hand myHand = board.getHandOf(direction);
		Point handLocationCenter = null;
		switch (direction) {
		case NORTH:
			handLocationCenter = new Point(NORTH_X_CENTER, NORTH_Y_CENTER);
			break;
		case EAST:
			handLocationCenter = new Point(EAST_X_CENTER, EAST_Y_CENTER);
			break;
		case SOUTH:
			handLocationCenter = new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER);
			break;
		case WEST:
			handLocationCenter = new Point(WEST_X_CENTER, WEST_Y_CENTER);
			break;
		}
		new HandElement(myHand, contentPane, null, handLocationCenter, new Player("Choosing: "));
	}
}
