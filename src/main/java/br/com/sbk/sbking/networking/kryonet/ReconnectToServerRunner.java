package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class ReconnectToServerRunner implements Runnable {

  private KryonetSBKingClient kryonetSBKingClient;
  private SBKingClient sbkingClient;

  public ReconnectToServerRunner(KryonetSBKingClient kryonetSBKingClient) {
    this.kryonetSBKingClient = kryonetSBKingClient;
  }

  @Override
  public void run() {
    try {
      LOGGER.info("Reconnecting: ");
      kryonetSBKingClient.reconnect();
      kryonetSBKingClient.getSbkingClient().sendNickname();
      LOGGER.info("Reconnected and sent nickname.");
    } catch (IOException ex) {
      LOGGER.fatal("Could not reconnect to server");
      LOGGER.fatal(ex);
      System.exit(ErrorCodes.COULD_NOT_CONNECT_TO_SERVER);
    }
  }

}
