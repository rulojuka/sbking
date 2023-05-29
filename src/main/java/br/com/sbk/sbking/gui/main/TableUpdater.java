package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class TableUpdater implements Runnable {

  private static final int UPDATE_TABLES_INTERVAL = 5000;
  private static final int WAIT_FOR_RESPONSE_TIME = 100;
  private boolean shouldStop = false;
  List<LobbyScreenTableDTO> tables = null;
  private SBKingClient sbkingClient;

  public TableUpdater(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void run() {
    while (!this.shouldStop) {
      this.sbkingClient.sendGetTables(); // This would be better locking but we don't look for a response.
      try { // So we have to wait a while to receive the response
        Thread.sleep(WAIT_FOR_RESPONSE_TIME);
      } catch (InterruptedException e) {
        LOGGER.error(e);
      }
      this.tables = this.sbkingClient.getTables();
      LOGGER.info("The current tables is:" + tables);
      try {
        Thread.sleep(UPDATE_TABLES_INTERVAL);
      } catch (InterruptedException e) {
        LOGGER.error(e);
      }
    }
  }

  public List<LobbyScreenTableDTO> getTables() {
    List<LobbyScreenTableDTO> tablesAuxiliar = this.tables;
    this.tables = null;
    return tablesAuxiliar;
  }

  public void shouldStop() {
    this.shouldStop = true;
  }
}
