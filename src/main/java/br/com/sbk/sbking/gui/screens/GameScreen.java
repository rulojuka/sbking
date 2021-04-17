package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.painters.PainterFactory;
import br.com.sbk.sbking.networking.client.SBKingClient;

public abstract class GameScreen {

  protected PainterFactory painterFactory;
  protected SBKingClient sbkingClient;

  public GameScreen(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
    this.painterFactory = new PainterFactory(sbkingClient);
  }

  protected void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.debug(e);
    }
  }

}
