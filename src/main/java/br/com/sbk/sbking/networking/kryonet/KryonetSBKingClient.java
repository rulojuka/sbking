package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.AcceptClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTableSpectatorsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTablesMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.RejectClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.UndoMessage;

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

  public void sendChoosePositiveMessage() {
    this.sendMessage(new ChoosePositiveMessage());
  }

  public void sendChooseNegativeMessage() {
    this.sendMessage(new ChooseNegativeMessage());
  }

  public void sendChooseGameModeOrStrain(String gameModeOrStrain) {
    this.sendMessage(new ChooseGameModeOrStrainMessage(gameModeOrStrain));
  }

  public void sendUndo() {
    this.sendMessage(new UndoMessage());
  }

  public void sendClaim() {
    this.sendMessage(new ClaimMessage());
  }

  public void sendGetTablesMessage() {
    this.sendMessage(new GetTablesMessage());
  }

  public void sendAcceptClaim() {
    this.sendMessage(new AcceptClaimMessage());
  }

  public void sendRejectClaim() {
    this.sendMessage(new RejectClaimMessage());
  }

  public void sendGetTableSpectators() {
    this.sendMessage(new GetTableSpectatorsMessage());
  }

}
