package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourIdIsMessage;
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
      LOGGER.error("Failed to send message: {}", message);
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
      LOGGER.debug("Sending {} to {}", message.getClass().toString(), connectionWithIdentifier.getIdentifier());
      connectionWithIdentifier.sendTCP(message);
    }
  }

  private void sendMany(SBKingMessage message, Iterable<UUID> receivers) {
    StreamSupport.stream(receivers.spliterator(), false).forEach(clientId -> this.sendOneTo(message, clientId));
  }

  public void sendGameModeOrStrainChooserTo(Direction direction, Iterable<UUID> receivers) {
    this.sendMany(new GameModeOrStrainChooserMessage(direction), receivers);
  }

  public void sendPositiveOrNegativeChooserTo(Direction direction, Iterable<UUID> receivers) {
    this.sendMany(new PositiveOrNegativeChooserMessage(direction), receivers);
  }

  public void sendPositiveOrNegativeTo(String positiveOrNegative, Iterable<UUID> receivers) {
    this.sendMany(new PositiveOrNegativeMessage(positiveOrNegative), receivers);
  }

  public void sendInitializeDealTo(Iterable<UUID> receivers) {
    this.sendMany(new InitializeDealMessage(), receivers);
  }

  public void sendInvalidRulesetTo(Iterable<UUID> receivers) {
    this.sendMany(new InvalidRulesetMessage(), receivers);
  }

  public void sendValidRulesetTo(Iterable<UUID> receivers) {
    this.sendMany(new ValidRulesetMessage(), receivers);
  }

  public String getNicknameFromIdentifier(UUID identifier) {
    return this.sbkingServer.getNicknameFromIdentifier(identifier);
  }

  public void sendYourIdIsTo(UUID playerIdentifier) {
    this.sendOneTo(new YourIdIsMessage(playerIdentifier.toString()), playerIdentifier);
  }

}
