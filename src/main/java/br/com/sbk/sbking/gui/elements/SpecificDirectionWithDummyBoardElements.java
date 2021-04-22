package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpecificDirectionWithDummyBoardElements {

  public SpecificDirectionWithDummyBoardElements(Direction playerDirection, Deal deal, Container container,
      ActionListener actionListener, Direction dummy, boolean dummyVisible) {

    for (Direction currentHandDirection : Direction.values()) {
      boolean isClaimer = this.isClaimer(deal, currentHandDirection);
      boolean shouldDrawVisible = this.shouldDrawVisible(playerDirection, currentHandDirection, dummy, dummyVisible)
          || isClaimer;
      new HandElement(deal.getHandOf(currentHandDirection), container, actionListener,
          FrameConstants.pointOfDirection.get(currentHandDirection), deal.getPlayerOf(currentHandDirection),
          shouldDrawVisible, currentHandDirection);
    }

    boolean isMyTurn = deal.getCurrentPlayer().equals(playerDirection);
    new CurrentPlayerElement(deal.getCurrentPlayer(), container, isMyTurn);

    new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

    new TrickElement(deal.getCurrentTrick(), container, new Point(container.getWidth() / 2, container.getHeight() / 2));

    new RulesetElement(deal.getRuleset(), container, new Point(150, 10));

    new UndoElement(container, new Point(150, container.getHeight() - 50), actionListener);

    new ClaimElement(deal.getClaimer(), deal.getCurrentPlayer(), deal.getIsPartnershipGame(), container,
        new Point(container.getWidth() - 150, container.getHeight() - 50), actionListener);
  }

  private boolean shouldDrawVisible(Direction playerDirection, Direction currentDirection, Direction dummy,
      boolean dummyVisible) {
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
      return false;
    } else {
      return currentDirection == playerDirection;
    }
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
