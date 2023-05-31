package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTablesMessage;

public class KryonetSBKingClient extends Client {

  private SBKingClient sbkingClient;

  public SBKingClient getSbkingClient() {
    return sbkingClient;
  }

  public void setSbkingClient(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  private void sendMessage(SBKingMessage message) {
    LOGGER.debug("Sending " + message.getClass().toString() + " to server.");
    this.sendTCP(message);
  }

  public void sendGetTablesMessage() {
    this.sendMessage(new GetTablesMessage());
  }

}
