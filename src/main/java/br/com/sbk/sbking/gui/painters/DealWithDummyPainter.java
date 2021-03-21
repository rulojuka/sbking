package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.SpecificDirectionWithDummyBoardElements;

public class DealWithDummyPainter extends DealPainter {

  private Direction dummy;
  private boolean dummyVisible = false;

  public DealWithDummyPainter(ActionListener actionListener, Direction direction, Deal deal, Direction dummy,
      boolean dummyVisible) {
    super(actionListener, direction, deal);
    this.dummy = dummy;
    this.dummyVisible = dummyVisible;
  }

  public DealWithDummyPainter(ActionListener actionListener, Direction direction, Board board, Direction dummy,
      boolean dummyVisible) {
    super(actionListener, direction, board);
    this.dummy = dummy;
    this.dummyVisible = dummyVisible;
  }

  @Override
  public void paint(Container contentPane) {
    logger.info("Painting deal that contains this trick: " + deal.getCurrentTrick());
    contentPane.removeAll();

    new SpecificDirectionWithDummyBoardElements(this.direction, this.deal, contentPane, this.actionListener, this.dummy,
        this.dummyVisible);

    contentPane.validate();
    contentPane.repaint();
  }

}
