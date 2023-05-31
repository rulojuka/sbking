package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GetTableSpectatorsResponseMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GetTablesResponseMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourDirectionIsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourIdIsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourTableIsMessage;
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

  private void sendMany(SBKingMessage message, Iterable<UUID> receivers) {
    StreamSupport.stream(receivers.spliterator(), false).forEach(clientId -> this.sendOneTo(message, clientId));
  }

  public void sendFinishDealTo(Iterable<UUID> receivers) {
    this.sendMany(new FinishDealMessage(), receivers);
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

  public void sendDealTo(Deal deal, Iterable<UUID> receivers) {
    this.sendMany(new DealMessage(deal), receivers);
  }

  public void sendDealTo(Deal deal, UUID playerIdentifier) {
    this.sendOneTo(new DealMessage(deal), playerIdentifier);
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

  public void sendYourTableIsTo(String gameName, UUID playerIdentifier) {
    this.sendOneTo(new YourTableIsMessage(gameName), playerIdentifier);
  }

  public void sendTablesTo(List<LobbyScreenTableDTO> tablesDTO, UUID playerIdentifier) {
    this.sendOneTo(new GetTablesResponseMessage(tablesDTO), playerIdentifier);
  }

  public void sendSpectatorsTo(List<String> spectatorNames, UUID playerIdentifier) {
    this.sendOneTo(new GetTableSpectatorsResponseMessage(spectatorNames), playerIdentifier);
  }

  public void sendYourIdIsTo(UUID playerIdentifier) {
    this.sendOneTo(new YourIdIsMessage(playerIdentifier.toString()), playerIdentifier);
  }

}
