package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.painters.SetNicknameScreenPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WelcomeScreen implements SBKingScreen {

  private boolean connectedToServer = false;
  private SBKingClient sbkingClient;

  private static final int WAIT_FOR_SERVER_MESSAGE_IN_MILISECONDS = 10;

  public WelcomeScreen(SBKingClient sbkingClient) {
    super();
    this.sbkingClient = sbkingClient;
  }

  public void setAndSendNickname(String nickname) {
    this.sbkingClient.setNickname(nickname);
    this.sbkingClient.sendNickname();
  }

  public boolean isConnectedToServer() {
    return connectedToServer;
  }

  public SBKingClient getSBKingClient() {
    return sbkingClient;
  }

  @Override
  public void runAt(SBKingClientJFrame sbkingClientJFrame) {
    LOGGER.debug("Starting to paint WelcomeScreen");
    sbkingClientJFrame.paintPainter(new SetNicknameScreenPainter(this));
    LOGGER.debug("Finished painting WelcomeScreen");

    LOGGER.info("Waiting for nickname to be set");
    while (!this.sbkingClient.isNicknameSet()) {
      sleepFor(WAIT_FOR_SERVER_MESSAGE_IN_MILISECONDS);
    }
    LOGGER.info("nicknameSet is true. Leaving WelcomeScreen");
  }

  private void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.error(e);
    }
  }

}
