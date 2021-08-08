package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.TableUpdater;
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
    List<LobbyScreenTableDTO> updatedTables;
    List<LobbyScreenTableDTO> tables;
    LOGGER.info("Entered Lobby Screen");
    tables = this.getTables();
    sbkingClientJFrame
        .paintPainter(new LobbyScreenPainter(tables, this.sbkingClient.getActionListener(), this.sbkingClient));

    TableUpdater tableUpdater = new TableUpdater(sbkingClient);
    Thread tableUpdaterThread = new Thread(tableUpdater, "table-updater");
    tableUpdaterThread.start();

    LOGGER.info("Waiting to receive gameName from server.");
    while (this.sbkingClient.getGameName() == null) {
      updatedTables = tableUpdater.getTables();
      if (updatedTables != null && !updatedTables.equals(tables)) {
        LOGGER.info("Updated tables!");
        tables = updatedTables;
        sbkingClientJFrame
            .paintPainter(new LobbyScreenPainter(tables, this.sbkingClient.getActionListener(), this.sbkingClient));
      }
      sleepFor(100);
    }
    tableUpdater.shouldStop();
  }

  public List<LobbyScreenTableDTO> getTables() {
    List<LobbyScreenTableDTO> tables = null;
    while (tables == null) {
      tables = this.sbkingClient.getTables();
      sleepFor(100);
      LOGGER.info("Waiting to get tables from server.");
    }
    return tables;
  }

  private void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.error(e);
    }
  }
}
