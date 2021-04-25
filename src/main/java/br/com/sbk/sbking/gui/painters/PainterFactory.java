package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class PainterFactory {

  private SBKingClient sbkingClient;

  public PainterFactory(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  public Painter getDealPainter(Deal deal, Direction direction, ActionListener actionListener) {
    return new DealPainter(actionListener, direction, deal);
  }

  public Painter getSpectatorPainter(Deal deal, ActionListener actionListener) {
    if (deal == null) {
      LOGGER.error("Deal should not be null here.");
      return null;
    } else {
      return new SpectatorPainter(actionListener, deal);
    }
  }

  public Painter getWaitingForChoosingGameModeOrStrainPainter(Direction direction, Direction chooser,
      boolean isPositive) {
    return new WaitingForChoosingGameModeOrStrainPainter(direction, chooser, isPositive, this.sbkingClient,
        this.sbkingClient.getCurrentGameScoreboard());
  }

  public Painter getWaitingForChoosingPositiveOrNegativePainter(Direction direction, Direction chooser) {
    return new WaitingForChoosingPositiveOrNegativePainter(direction, chooser, this.sbkingClient,
        this.sbkingClient.getCurrentGameScoreboard());
  }

  public Painter getDealWithDummyPainter(Deal deal, Direction direction, ActionListener actionListener) {
    boolean dummyVisible = this.sbkingClient.getDeal().isDummyOpen();
    Direction dummy = this.sbkingClient.getDeal().getDummy();
    return new DealWithDummyPainter(actionListener, direction, deal, dummy, dummyVisible);
  }

}
