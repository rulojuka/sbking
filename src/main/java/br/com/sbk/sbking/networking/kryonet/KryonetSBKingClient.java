package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.BoardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.TextMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.YourDirectionIsMessage;

public class KryonetSBKingClient extends Client {

  private SBKingClient sbkingClient;

  public KryonetSBKingClient(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

  protected void onMessage(SBKingMessage message) {
    LOGGER.debug("Entered --onMessage--");
    Object content = message.getContent();
    if (message instanceof TextMessage) {
      LOGGER.info("Received message from server: " + content);
    } else if (message instanceof BoardMessage) {
      this.sbkingClient.setCurrentBoard((Board) content);
    } else if (message instanceof DealMessage) {
      this.sbkingClient.setCurrentDeal((Deal) content);
    } else if (message instanceof YourDirectionIsMessage) {
      this.sbkingClient.initializeDirection((Direction) content);
    } else if (message instanceof PositiveOrNegativeChooserMessage) {
      this.sbkingClient.setPositiveOrNegativeChooser((Direction) content);
    } else if (message instanceof PositiveOrNegativeMessage) {
      boolean isPositive = (Boolean) content;
      if (isPositive) {
        this.sbkingClient.selectedPositive();
      } else {
        this.sbkingClient.selectedNegative();
      }
    } else if (message instanceof GameModeOrStrainChooserMessage) {
      this.sbkingClient.setGameModeOrStrainChooser((Direction) content);
    } else if (message instanceof InitializeDealMessage) {
      this.sbkingClient.initializeDeal();
    } else if (message instanceof FinishDealMessage) {
      this.sbkingClient.finishDeal();
    } else if (message instanceof InvalidRulesetMessage) {
      this.sbkingClient.setRulesetValid(false);
    } else if (message instanceof ValidRulesetMessage) {
      this.sbkingClient.setRulesetValid(true);
    } else if (message instanceof IsSpectatorMessage) {
      this.sbkingClient.setSpectator(true);
    } else if (message instanceof IsNotSpectatorMessage) {
      this.sbkingClient.setSpectator(false);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
    }

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

}
