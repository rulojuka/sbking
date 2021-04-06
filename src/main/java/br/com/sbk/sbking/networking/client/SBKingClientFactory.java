package br.com.sbk.sbking.networking.client;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.gui.listeners.ClientActionListener;
import br.com.sbk.sbking.networking.kryonet.KryonetClientFactory;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingClient;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingClientActionListener;

public class SBKingClientFactory {

  public static SBKingClient createWithKryonetConnection(String nickname, String hostname, int port) {
    SBKingClient sbKingClient = new SBKingClient();
    KryonetSBKingClient kryonetSBKingClient = KryonetClientFactory.getRegisteredClient(sbKingClient);
    sbKingClient.setPlayCardActionListener(
        new ClientActionListener(new KryonetSBKingClientActionListener(kryonetSBKingClient)));
    LOGGER.info("Trying to connect.");
    try {
      kryonetSBKingClient.connect(50000, hostname, port);
      LOGGER.info("Connected.");
      // Server communication after connection can go here, or in
      // Listener#connected().
    } catch (IOException ex) {
      ex.printStackTrace();
      System.exit(ErrorCodes.COULD_NOT_CONNECT_TO_SERVER);
    }
    sbKingClient.setKryonetSBKingClient(kryonetSBKingClient);
    sbKingClient.sendSetNickname(nickname);
    return sbKingClient;
  }

}
