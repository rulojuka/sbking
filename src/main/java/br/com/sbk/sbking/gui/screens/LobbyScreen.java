package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.painters.LobbyScreenPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class LobbyScreen implements SBKingScreen {

  private SBKingClient sbkingClient;

  public LobbyScreen(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  @Override
  public void runAt(SBKingClientJFrame sbkingClientJFrame) {

    LOGGER.info("Entered Lobby Screen");
    sbkingClientJFrame.paintPainter(new LobbyScreenPainter(this.sbkingClient));
    LOGGER.info("isDirectionOrSpectatorSet. Leaving Lobby Screen");
  }

}