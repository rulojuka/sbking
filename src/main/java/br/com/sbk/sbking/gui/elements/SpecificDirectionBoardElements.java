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
            boolean isClaimer = this.isClaimer(deal, currentDirection);
            boolean isVisible = isClaimer || currentDirection.equals(direction);
            new HandElement(deal.getHandOf(currentDirection), container, actionListener,
                    FrameConstants.pointOfDirection.get(currentDirection), deal.getPlayerOf(currentDirection),
                    isVisible, currentDirection);
        }

        boolean isMyTurn = deal.getCurrentPlayer().equals(direction);
        new CurrentPlayerElement(deal.getCurrentPlayer(), container, isMyTurn);

        new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

        new TrickElement(deal.getCurrentTrick(), container,
                new Point(container.getWidth() / 2, container.getHeight() / 2));

        new RulesetElement(deal.getRuleset(), container, new Point(150, 10));

        new UndoElement(container, new Point(150, container.getHeight() - 50), actionListener);

        new ClaimElement(deal.getClaimer(), deal.getCurrentPlayer(), deal.getIsPartnershipGame(), container,
                new Point(container.getWidth() - 150, container.getHeight() - 50), actionListener);
    }

    private boolean isClaimer(Deal deal, Direction handDirection) {
        Direction claimer = deal.getClaimer();
        Boolean isPartnershipGame = deal.getIsPartnershipGame();
        if (claimer != null) {
            if (isPartnershipGame) {
                Direction claimerPartner = claimer.next(2);
                return handDirection.equals(claimer) || handDirection.equals(claimerPartner);
            }
            return handDirection.equals(claimer);
        }
        return false;
    }

}
