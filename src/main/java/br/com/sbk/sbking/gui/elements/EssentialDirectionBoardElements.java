package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class EssentialDirectionBoardElements {

    public EssentialDirectionBoardElements(Deal deal, Container container, ActionListener actionListener) {
        for (Direction direction : Direction.values()) {
            new HandElement(deal, container, actionListener, FrameConstants.pointOfDirection.get(direction),
                    deal.getPlayerOf(direction), true, direction);
        }

        new RulesetElement(deal.getRuleset(), container, new Point(150, 150));

        new LeaveTableElement(container, new Point(150, 50), actionListener);
    }

}
