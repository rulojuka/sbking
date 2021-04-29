package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.CreateTableMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTablesMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.JoinTableMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.LeaveTableMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.UndoMessage;

public class KryonetSBKingClient extends Client {

  private String nickname;

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

  public void setAndSendNickname(String nickname) {
    this.nickname = nickname;
    this.sendMessage(new SetNicknameMessage(nickname));
  }

  public void sendNickname() {
    this.sendMessage(new SetNicknameMessage(this.nickname));
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

  public void sendCreateTableMessage(String gameName) {
    this.sendMessage(new CreateTableMessage(gameName));
  }

  public void sendGetTablesMessage() {
    this.sendMessage(new GetTablesMessage());
  }

  public void sendJoinTable(UUID tableId) {
    this.sendMessage(new JoinTableMessage(tableId));
  }

  public void sendLeaveTable() {
    this.sendMessage(new LeaveTableMessage());
  }

}
