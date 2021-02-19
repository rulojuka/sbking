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
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class SpecificDirectionBoardElements {

	public SpecificDirectionBoardElements(Direction direction, Deal deal, Container container,
			ActionListener actionListener) {
		switch (direction) {
		case NORTH:
			new HandElement(deal.getHandOf(Direction.NORTH), container, actionListener,
					new Point(NORTH_X_CENTER, NORTH_Y_CENTER), deal.getPlayerOf(Direction.NORTH));
			break;
		case EAST:
			new HandElement(deal.getHandOf(Direction.EAST), container, actionListener,
					new Point(EAST_X_CENTER, EAST_Y_CENTER), deal.getPlayerOf(Direction.EAST));
			break;
		case SOUTH:
			new HandElement(deal.getHandOf(Direction.SOUTH), container, actionListener,
					new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER), deal.getPlayerOf(Direction.SOUTH));
			break;
		case WEST:
			new HandElement(deal.getHandOf(Direction.WEST), container, actionListener,
					new Point(WEST_X_CENTER, WEST_Y_CENTER), deal.getPlayerOf(Direction.WEST));
			break;

		}

		boolean isMyTurn = deal.getCurrentPlayer().equals(direction);
		new CurrentPlayerElement(deal.getCurrentPlayer(), container, isMyTurn);

		new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

		new TrickElement(deal.getCurrentTrick(), container,
				new Point(container.getWidth() / 2, container.getHeight() / 2));

		new RulesetElement(deal.getRuleset(), container, new Point(150, 10));
	}

}
