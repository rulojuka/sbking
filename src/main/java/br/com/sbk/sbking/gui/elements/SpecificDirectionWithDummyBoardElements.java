package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpecificDirectionWithDummyBoardElements {

  public SpecificDirectionWithDummyBoardElements(Direction direction, Deal deal, Container container,
      ActionListener actionListener, Direction dummy, boolean dummyVisible) {

    for (Direction currentDirection : Direction.values()) {
      boolean shouldDrawVisible = this.shouldDrawVisible(direction, currentDirection, dummy, dummyVisible);
      new HandElement(deal.getHandOf(currentDirection), container, actionListener,
          FrameConstants.pointOfDirection.get(currentDirection), deal.getPlayerOf(currentDirection), shouldDrawVisible,
          direction);
    }

    boolean isMyTurn = deal.getCurrentPlayer().equals(direction);
    new CurrentPlayerElement(deal.getCurrentPlayer(), container, isMyTurn);

    new ScoreboardElement(deal, container, new Point(container.getWidth() - 150, 10));

    new TrickElement(deal.getCurrentTrick(), container, new Point(container.getWidth() / 2, container.getHeight() / 2));

    new RulesetElement(deal.getRuleset(), container, new Point(150, 10));
  }

  private boolean shouldDrawVisible(Direction playerDirection, Direction currentDirection, Direction dummy,
      boolean dummyVisible) {
    if (dummyVisible) {
      return currentDirection == playerDirection || currentDirection == dummy;
    } else {
      return currentDirection == playerDirection;
    }
  }

}
