package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpecificDirectionBoardElements {

    public SpecificDirectionBoardElements(Direction direction, Deal deal, Container container,
            ActionListener actionListener) {

        for (Direction currentDirection : Direction.values()) {
            if (currentDirection.equals(direction)) {
                new HandElement(deal.getHandOf(currentDirection), container, actionListener,
                        FrameConstants.pointOfDirection.get(currentDirection), deal.getPlayerOf(currentDirection), true,
                        currentDirection);
            } else {
                new HandElement(deal.getHandOf(currentDirection), container, actionListener,
                        FrameConstants.pointOfDirection.get(currentDirection), deal.getPlayerOf(currentDirection),
                        false, currentDirection);
            }
        }

        boolean isMyTurn = deal.getCurrentPlayer().equals(direction);
        new CurrentPlayerElement(deal.getCurrentPlayer(), container, isMyTurn);

        new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

        new TrickElement(deal.getCurrentTrick(), container,
                new Point(container.getWidth() / 2, container.getHeight() / 2));

        new RulesetElement(deal.getRuleset(), container, new Point(150, 10));

        new UndoElement(container, new Point(150, container.getHeight() - 50), actionListener);

        new ClaimElement(deal.getClaimer(), deal.getCurrentPlayer(), container,
                new Point(container.getWidth() - 150, container.getHeight() - 50), actionListener);
    }

}
