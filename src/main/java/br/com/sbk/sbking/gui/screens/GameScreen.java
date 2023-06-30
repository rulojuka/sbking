package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.painters.PainterFactory;
import br.com.sbk.sbking.networking.client.SBKingClient;

public abstract class GameScreen implements SBKingScreen {

  protected PainterFactory painterFactory;
  protected SBKingClient sbkingClient;

  protected static final int WAIT_FOR_SERVER_MESSAGE_IN_MILISECONDS = 10;
  protected static final int WAIT_FOR_REDRAW_IN_MILISECONDS = 10;

  public GameScreen(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
    this.painterFactory = new PainterFactory(sbkingClient);
  }

  protected void sleepFor(int miliseconds) {
    try {
      if (miliseconds > 10) {
        LOGGER.info("Sleeping for {} miliseconds.", miliseconds);
      }
      Thread.sleep(miliseconds);
      if (miliseconds > 10) {
        LOGGER.info("Woke up.");
      }
    } catch (InterruptedException e) {
      LOGGER.error(e);
    }
  }

  protected boolean checkIfStillIsOnGameScreen() {
    return sbkingClient.getGameName() != null;
  }

  protected void waitForDirection() {
    LOGGER.info("Waiting for sbkingClient.isDirectionSet() to be true");
    while (!this.sbkingClient.isDirectionOrSpectatorSet()) {
      sleepFor(WAIT_FOR_SERVER_MESSAGE_IN_MILISECONDS);
    }
  }

}
