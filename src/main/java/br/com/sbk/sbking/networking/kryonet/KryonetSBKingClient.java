package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.UndoMessage;

public class KryonetSBKingClient extends Client {

  private SBKingClient sbkingClient;

  public KryonetSBKingClient(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  private void sendMessage(SBKingMessage message) {
    LOGGER.debug("Sending " + message.getClass().toString() + " to server.");
    this.sendTCP(message);
  }

  public void sendCard(Card card) {
    this.sendMessage(new PlayCardMessage(card));
  }

  public void sitOrLeave(Direction direction) {
    this.sendMessage(new MoveToSeatMessage(direction));
  }

  public void sendSetNickname(String nickname) {
    this.sendMessage(new SetNicknameMessage(nickname));
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

}
