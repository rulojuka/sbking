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

  private static final int DELAY_FOR_UPDATING_TABLES = 1000;

  public LobbyScreen(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void runAt(SBKingClientJFrame sbkingClientJFrame) {
    LOGGER.info("Entered Lobby Screen");
    this.paintEverything(sbkingClientJFrame);

    TableUpdater tableUpdater = new TableUpdater(sbkingClient);
    Thread tableUpdaterThread = new Thread(tableUpdater, "table-updater");
    tableUpdaterThread.start();

    LOGGER.info("Waiting to receive gameName from server.");
    while (this.sbkingClient.getGameName() == null) {
      if (this.sbkingClient.shouldRedrawTables()) {
        this.paintEverything(sbkingClientJFrame);
      }
      sleepFor(DELAY_FOR_UPDATING_TABLES);
    }
    tableUpdater.shouldStop();
  }

  public List<LobbyScreenTableDTO> getTables() {
    return this.sbkingClient.getTables();
  }

  private void paintEverything(SBKingClientJFrame sbkingClientJFrame) {
    sbkingClientJFrame
        .paintPainter(
            new LobbyScreenPainter(this.getTables(), this.sbkingClient.getActionListener(), this.sbkingClient));
  }

  private void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.error(e);
    }
  }
}
