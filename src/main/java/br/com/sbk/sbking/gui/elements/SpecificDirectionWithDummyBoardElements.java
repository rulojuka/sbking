package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpecificDirectionWithDummyBoardElements {

  public SpecificDirectionWithDummyBoardElements(Direction playerDirection, Deal deal, Container container,
      ActionListener actionListener, Direction dummy, boolean dummyVisible, List<String> spectators) {

    for (Direction currentHandDirection : Direction.values()) {
      boolean shouldDrawVisible = this.shouldDrawVisible(playerDirection, currentHandDirection, dummy, dummyVisible,
          deal);
      new HandElement(deal, container, actionListener, FrameConstants.pointOfDirection.get(currentHandDirection),
          deal.getPlayerOf(currentHandDirection), shouldDrawVisible, currentHandDirection);
    }

    new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

    new TrickElement(deal.getCurrentTrick(), container, new Point(container.getWidth() / 2, container.getHeight() / 2));

    new RulesetElement(deal.getRuleset(), container, new Point(150, 150));

    if (!playerDirection.equals(dummy)) {
      new UndoElement(container, new Point(150, container.getHeight() - 50), actionListener);
    }

    new LeaveTableElement(container, new Point(150, 50), actionListener);

    new ClaimElement(deal.getClaimer(), container, new Point(320, container.getHeight() - 50), actionListener);

    new AcceptClaimElement(deal.getClaimer(), playerDirection, deal.getIsPartnershipGame(), deal.getAcceptedClaimMap(),
        container, new Point(container.getWidth() - 150, container.getHeight() - 50), actionListener);

    new RejectClaimElement(deal.getClaimer(), playerDirection, deal.getIsPartnershipGame(), deal.getAcceptedClaimMap(),
        container, new Point(container.getWidth() - 150, container.getHeight() - 50), actionListener);

    new SpectatorsElement(container, FrameConstants.spectatorNamesPosition, spectators);
  }

  private boolean shouldDrawVisible(Direction playerDirection, Direction currentDirection, Direction dummy,
      boolean dummyVisible, Deal deal) {
    if (dummyVisible) {
      if (currentDirection == playerDirection) {
        return true;
      }
      if (currentDirection == dummy) {
        return true;
      }
      if (playerDirection == dummy && currentDirection == dummy.next(2)) {
        return true;
      }
      return deal.isFinished() || currentDirection.equals(deal.getClaimer());
    } else {
      return currentDirection == playerDirection || deal.isFinished() || currentDirection.equals(deal.getClaimer());
    }
  }

}
