package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class AllDirectionBoardElements {

        public AllDirectionBoardElements(Deal deal, Container container, ActionListener actionListener,
                        List<String> spectators, String gameName) {
                for (Direction direction : Direction.values()) {
                        new HandElement(deal, container, actionListener, FrameConstants.getPointOfDirection(direction),
                                        deal.getPlayerOf(direction), true, direction);
                }

                new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

                new TrickElement(deal.getCurrentTrick(), container,
                                new Point(container.getWidth() / 2, container.getHeight() / 2));

                new RulesetElement(deal.getRuleset(), container, FrameConstants.getRulesetPosition());

                new LeaveTableElement(container, new Point(150, 50), actionListener);

                new SpectatorsElement(container, FrameConstants.getSpectatorNamesPosition(), spectators);

                GameNameElement.addGameNameToContainerAtPosition(gameName, container,
                                new Point(FrameConstants.getHalfWidth(), 10));
        }

}
