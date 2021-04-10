package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
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

  private List<ConnectionWithIdentifier> connections;
  private SBKingServer sbkingServer;

  public KryonetSBKingServer(SBKingServer sbkingServer) {
    this.sbkingServer = sbkingServer;
    this.connections = new ArrayList<ConnectionWithIdentifier>();
  }

  public void addConnection(ConnectionWithIdentifier connectionWithIdentifier) {
    UUID identifier = connectionWithIdentifier.getIdentifier();
    this.connections.add(connectionWithIdentifier);
    this.sbkingServer.addUnnammedPlayer(identifier);
    this.sbkingServer.addSpectator(identifier);
  }

  public void removeConnection(ConnectionWithIdentifier connectionWithIdentifier) {
    this.connections.remove(connectionWithIdentifier);
    this.sbkingServer.removePlayer(connectionWithIdentifier.getIdentifier());
  }

  private void sendOneTo(SBKingMessage message, UUID playerIdentifier) {
    ConnectionWithIdentifier connectionToSend = this.connections.stream()
        .filter(connection -> playerIdentifier.equals(connection.getIdentifier())).findFirst().orElse(null);
    if (connectionToSend != null) {
      this.sendOneTo(message, connectionToSend);
    } else {
      String errorMessage = "Did not found connection with UUID:" + playerIdentifier;
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException(errorMessage);
      LOGGER.error(errorMessage);
      LOGGER.error("Failed to send message:" + message);
      LOGGER.error(illegalArgumentException);
      LOGGER.error(illegalArgumentException.getStackTrace());
    }
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
    this.sendToAllTCP(message); // This will need to change when there are multiple tables.
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
