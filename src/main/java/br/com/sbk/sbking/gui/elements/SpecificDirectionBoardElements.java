package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class SpecificDirectionBoardElements {

	private static final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private static final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private static final int SOUTH_X = NORTH_X;
	private static final int EAST_X = NORTH_X + 300;
	private static final int WEST_X = NORTH_X - 300;
	private static final int NORTH_Y = 120;
	private static final int SOUTH_Y = 470;
	private static final int EAST_Y = 300;
	private static final int WEST_Y = EAST_Y;

	public SpecificDirectionBoardElements(Direction direction, Deal deal, Container container,
			ActionListener actionListener) {
		switch (direction) {
		case NORTH:
			new HandElement(deal.getHandOf(Direction.NORTH), container, actionListener, new Point(NORTH_X, NORTH_Y));
			break;
		case EAST:
			new HandElement(deal.getHandOf(Direction.EAST), container, actionListener, new Point(EAST_X, EAST_Y));
			break;
		case SOUTH:
			new HandElement(deal.getHandOf(Direction.SOUTH), container, actionListener, new Point(SOUTH_X, SOUTH_Y));
			break;
		case WEST:
			new HandElement(deal.getHandOf(Direction.WEST), container, actionListener, new Point(WEST_X, WEST_Y));
			break;

		}

		new CurrentPlayerElement(deal.getCurrentPlayer(), container,
				discoverCurrentPlayerElementLocation(deal.getCurrentPlayer()));

		new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

		new TrickElement(deal.getCurrentTrick(), container,
				new Point(container.getWidth() / 2, container.getHeight() / 2));

		new RulesetElement(deal.getRuleset(), container, new Point(150, 10));
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
