package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.SpecificDirectionWithDummyBoardElements;

public class DealWithDummyPainter extends DealPainter {

  private Direction dummy;
  private boolean dummyVisible = false;

  public DealWithDummyPainter(ActionListener actionListener, Direction direction, Deal deal, Direction dummy,
      boolean dummyVisible, List<String> spectators) {
    super(actionListener, direction, deal, null);
    this.dummy = dummy;
    this.dummyVisible = dummyVisible;
    this.spectators = spectators;
  }

  @Override
  public void paint(Container contentPane) {
    LOGGER.trace("Painting deal that contains this trick: " + deal.getCurrentTrick());
    contentPane.removeAll();

    new SpecificDirectionWithDummyBoardElements(this.direction, this.deal, contentPane, this.actionListener, this.dummy,
        this.dummyVisible, this.spectators);

    contentPane.validate();
    contentPane.repaint();
  }

}
