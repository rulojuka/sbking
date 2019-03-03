package gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import core.Board;
import core.Direction;

public class BoardElements {

	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
	private final int EAST_X = NORTH_X + 300;
	private final int WEST_X = NORTH_X - 300;
	private final int NORTH_Y = 120;
	private final int SOUTH_Y = 470;
	private final int EAST_Y = 300;
	private final int WEST_Y = EAST_Y;

	public BoardElements(Board board, Container container, ActionListener actionListener) {
		new HandElement(board.getNorthHand(), container, actionListener, new Point(NORTH_X, NORTH_Y));
		new HandElement(board.getEastHand(), container, actionListener, new Point(EAST_X, EAST_Y));
		new HandElement(board.getSouthHand(), container, actionListener, new Point(SOUTH_X, SOUTH_Y));
		new HandElement(board.getWestHand(), container, actionListener, new Point(WEST_X, WEST_Y));

		new CurrentPlayerElement(board.getCurrentPlayer(), container,
				discoverCurrentPlayerElementLocation(board.getCurrentPlayer()));
		
		new ScoreboardElement(board,container, new Point(container.getWidth()-150,10));
		
		new TrickElement(board.getCurrentTrick(),container, new Point(container.getWidth()/2, container.getHeight()/2));
		
		new RulesetElement(board.getRuleset(), container, new Point(150,10));
	}

	private Point discoverCurrentPlayerElementLocation(Direction direction) {
		if (direction.isNorth()) {
			return new Point(NORTH_X, NORTH_Y - 30);
		} else if (direction.isEast()) {
			return new Point(EAST_X, EAST_Y - 30);
		} else if (direction.isSouth()) {
			return new Point(SOUTH_X, SOUTH_Y - 30);
		} else {
			return new Point(WEST_X, WEST_Y - 30);
		}

	}

}
