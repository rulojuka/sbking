package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.UndoMessage;
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
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.YourDirectionIsMessage;
import br.com.sbk.sbking.networking.server.SBKingServer;

public class KryonetSBKingServer extends Server {

  private Map<UUID, ConnectionWithIdentifier> connections = new HashMap<UUID, ConnectionWithIdentifier>();
  private SBKingServer sbkingServer;
  private UUID lastConnected;

  public KryonetSBKingServer(SBKingServer sbkingServer) {
    this.sbkingServer = sbkingServer;
  }

  public void addConnection(ConnectionWithIdentifier connectionWithIdentifier) {
    UUID identifier = connectionWithIdentifier.getIdentifier();
    this.connections.put(identifier, connectionWithIdentifier);
    this.lastConnected = identifier;
    this.sbkingServer.addUnnammedPlayer(identifier);
    this.sbkingServer.addSpectator(identifier);
  }

  public void removeConnection(ConnectionWithIdentifier connectionWithIdentifier) {
    this.connections.remove(connectionWithIdentifier.getIdentifier());
  }

  public UUID getLastConnected() {
    return this.lastConnected;
  }

  protected void onMessage(SBKingMessage message, ConnectionWithIdentifier connectionWithIdentifier) {
    LOGGER.debug("Entered --onMessage--");
    Object content = message.getContent();
    UUID playerIdentifier = connectionWithIdentifier.getIdentifier();
    if (message instanceof SetNicknameMessage) {
      this.sbkingServer.setNickname(playerIdentifier, (String) content);
    } else if (message instanceof PlayCardMessage) {
      this.sbkingServer.play((Card) content, playerIdentifier);
    } else if (message instanceof MoveToSeatMessage) {
      this.sbkingServer.moveToSeat((Direction) content, playerIdentifier);
    } else if (message instanceof ChoosePositiveMessage) {
      this.sbkingServer.choosePositive(playerIdentifier);
    } else if (message instanceof ChooseNegativeMessage) {
      this.sbkingServer.chooseNegative(playerIdentifier);
    } else if (message instanceof ChooseGameModeOrStrainMessage) {
      this.sbkingServer.chooseGameModeOrStrain((String) content, playerIdentifier);
    } else if (message instanceof UndoMessage) {
      this.sbkingServer.undo(playerIdentifier);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
    }
  }

  private void sendOneTo(SBKingMessage message, UUID playerIdentifier) {
    this.sendOneTo(message, this.connections.get(playerIdentifier));
  }

  private void sendOneTo(SBKingMessage message, ConnectionWithIdentifier connectionWithIdentifier) {
    if (connectionWithIdentifier == null) {
      String errorMessage = "connectionWithIdentifier should not be null";
      LOGGER.fatal(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    } else {
      LOGGER.debug("Sending " + message.getClass().toString() + " to " + connectionWithIdentifier.getIdentifier());
      connectionWithIdentifier.sendTCP(message);
    }
  }

  private void sendAll(SBKingMessage message) {
    LOGGER.debug("Sending everyone " + message.getClass().toString());
    for (ConnectionWithIdentifier connection : connections.values()) {
      connection.sendTCP(message);
    }
  }

  public void sendFinishDealAll() {
    this.sendAll(new FinishDealMessage());
  }

  public void sendDirectionTo(Direction direction, UUID playerIdentifier) {
    this.sendOneTo(new YourDirectionIsMessage(direction), playerIdentifier);
  }

  public void sendIsSpectatorTo(UUID playerIdentifier) {
    this.sendOneTo(new IsSpectatorMessage(), playerIdentifier);
  }

  public void sendIsNotSpectatorTo(UUID playerIdentifier) {
    this.sendOneTo(new IsNotSpectatorMessage(), playerIdentifier);
  }

  public void sendBoardAll(Board board) {
    this.sendAll(new BoardMessage(board));
  }

  public void sendDealAll(Deal deal) {
    this.sendAll(new DealMessage(deal));
  }

  public void sendGameModeOrStrainChooserAll(Direction direction) {
    this.sendAll(new GameModeOrStrainChooserMessage(direction));
  }

  public void sendPositiveOrNegativeChooserAll(Direction direction) {
    this.sendAll(new PositiveOrNegativeChooserMessage(direction));
  }

  public void sendPositiveOrNegativeAll(String positiveOrNegative) {
    this.sendAll(new PositiveOrNegativeMessage(positiveOrNegative));
  }

  public void sendInitializeDealAll() {
    this.sendAll(new InitializeDealMessage());
  }

  public void sendInvalidRulesetAll() {
    this.sendAll(new InvalidRulesetMessage());
  }

  public void sendValidRulesetAll() {
    this.sendAll(new ValidRulesetMessage());
  }

  public String getNicknameFromIdentifier(UUID identifier) {
    return this.sbkingServer.getNicknameFromIdentifier(identifier);
  }

  public boolean nobodyIsConnected() {
    return this.connections.isEmpty();
  }
}
