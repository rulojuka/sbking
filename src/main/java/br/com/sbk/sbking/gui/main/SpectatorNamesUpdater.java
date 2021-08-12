package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class SpectatorNamesUpdater implements Runnable {

  private static final int UPDATE_SPECTATOR_NAMES_TIMEOUT = 3000;
  private boolean shouldStop = false;
  List<String> spectatorNames = null;
  private SBKingClient sbkingClient;

  public SpectatorNamesUpdater(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void run() {
    while (!this.shouldStop) {
      this.sbkingClient.sendGetSpectatorNames();
      try {
        Thread.sleep(UPDATE_SPECTATOR_NAMES_TIMEOUT);
      } catch (InterruptedException e) {
        LOGGER.error(e);
      }
    }
  }

  public void shouldStop() {
    this.shouldStop = true;
    this.sbkingClient = null;
    this.spectatorNames = null;
  }
}
