package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class TableUpdater implements Runnable {

  private static final int UPDATE_TABLES_INTERVAL = 3000;
  private boolean shouldStop = false;
  private SBKingClient sbkingClient;

  public TableUpdater(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void run() {
    while (!this.shouldStop) {
      this.sbkingClient.sendGetTables();
      try {
        Thread.sleep(UPDATE_TABLES_INTERVAL);
      } catch (InterruptedException e) {
        LOGGER.error(e);
      }
    }
  }

  public void shouldStop() {
    this.shouldStop = true;
  }
}
