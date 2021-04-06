package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class AllDirectionBoardElements {

    public AllDirectionBoardElements(Deal deal, Container container, ActionListener actionListener) {
        for (Direction direction : Direction.values()) {
            new HandElement(deal.getHandOf(direction), container, actionListener,
                    FrameConstants.pointOfDirection.get(direction), deal.getPlayerOf(direction), true, direction);
        }

        new CurrentPlayerElement(deal.getCurrentPlayer(), container, false);

        new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

        new TrickElement(deal.getCurrentTrick(), container,
                new Point(container.getWidth() / 2, container.getHeight() / 2));

        new RulesetElement(deal.getRuleset(), container, new Point(150, 10));
    }

}
