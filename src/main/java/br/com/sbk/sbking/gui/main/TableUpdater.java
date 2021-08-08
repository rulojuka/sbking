package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class TableUpdater implements Runnable {

  private static final int UPDATE_TABLES_TIMEOUT = 5000;
  private boolean shouldStop = false;
  List<LobbyScreenTableDTO> tables = null;
  private SBKingClient sbkingClient;

  public TableUpdater(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void run() {
    while (!this.shouldStop) {
      this.sbkingClient.sendGetTables();
      this.tables = this.sbkingClient.getTables();
      LOGGER.info("The current tables is:" + tables);
      try {
        Thread.sleep(UPDATE_TABLES_TIMEOUT);
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
