package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.painters.LobbyScreenPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class LobbyScreen implements SBKingScreen {

  private SBKingClient sbkingClient;

  public LobbyScreen(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
    this.sbkingClient.sendGetTables();
  }

  @Override
  public void runAt(SBKingClientJFrame sbkingClientJFrame) {

    LOGGER.info("Entered Lobby Screen");
    while (this.sbkingClient.getTables() == null) {
      sleepFor(100);
      LOGGER.info("Waiting to get tables from server.");
    }
    sbkingClientJFrame.paintPainter(new LobbyScreenPainter(this.sbkingClient.getTables(),
        this.sbkingClient.getActionListener(), this.sbkingClient));
    LOGGER.info("Waiting to receive gameName from server.");
    while (this.sbkingClient.getGameName() == null) {
      sleepFor(100);
    }
  }

  private void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.error(e);
    }
  }
}
