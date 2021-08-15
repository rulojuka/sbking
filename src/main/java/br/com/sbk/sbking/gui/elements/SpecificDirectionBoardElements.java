package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpecificDirectionBoardElements {

        public SpecificDirectionBoardElements(Direction direction, Deal deal, Container container,
                        ActionListener actionListener, List<String> spectators) {

                for (Direction currentDirection : Direction.values()) {
                        boolean isVisible = currentDirection.equals(direction) || deal.isFinished()
                                        || currentDirection.equals(deal.getClaimer());
                        new HandElement(deal, container, actionListener,
                                        FrameConstants.pointOfDirection.get(currentDirection),
                                        deal.getPlayerOf(currentDirection), isVisible, currentDirection);
                }

                new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

                new TrickElement(deal.getCurrentTrick(), container,
                                new Point(container.getWidth() / 2, container.getHeight() / 2));

                new RulesetElement(deal.getRuleset(), container, FrameConstants.rulesetPosition);

                new UndoElement(container, new Point(150, container.getHeight() - 50), actionListener);

                new LeaveTableElement(container, new Point(150, 50), actionListener);

                new ClaimElement(deal.getClaimer(), container, new Point(320, container.getHeight() - 50),
                                actionListener);

                new AcceptClaimElement(deal.getClaimer(), direction, deal.getIsPartnershipGame(),
                                deal.getAcceptedClaimMap(), container, new Point(420, container.getHeight() - 50),
                                actionListener);

                new RejectClaimElement(deal.getClaimer(), direction, deal.getIsPartnershipGame(),
                                deal.getAcceptedClaimMap(), container, new Point(420, container.getHeight() - 50),
                                actionListener);

                new SpectatorsElement(container, FrameConstants.spectatorNamesPosition, spectators);
        }

}
