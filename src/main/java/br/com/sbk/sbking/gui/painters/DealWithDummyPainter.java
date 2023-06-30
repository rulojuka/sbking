package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.SpecificDirectionWithDummyBoardElements;

public class DealWithDummyPainter extends DealPainter {

  private boolean dummyVisible = false;

  public DealWithDummyPainter(ActionListener actionListener, Direction direction, Deal deal, Direction dummy,
      boolean dummyVisible, List<String> spectators, String gameName) {
    super(actionListener, direction, deal, spectators, gameName);
    this.dummyVisible = dummyVisible;
    this.spectators = spectators;
    this.gameName = gameName;
  }

  @Override
  public void paint(Container contentPane) {
    LOGGER.trace("Painting deal that contains this trick: {}", deal.getCurrentTrick());
    contentPane.removeAll();

    new SpecificDirectionWithDummyBoardElements(this.direction, this.deal, contentPane, this.actionListener,
        this.dummyVisible, this.spectators, this.gameName);

    contentPane.validate();
    contentPane.repaint();
  }

}
