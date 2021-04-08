package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;

public class Table {

  private Map<Direction, ClientGameSocket> playerSockets;
  private Collection<ClientGameSocket> spectatorSockets;
  private Map<UUID, ClientGameSocket> allSockets;
  private GameServer gameServer;

  public Table(GameServer gameServer) {
    this.gameServer = gameServer;
    this.playerSockets = new HashMap<Direction, ClientGameSocket>();
    this.spectatorSockets = new ArrayList<ClientGameSocket>();
    this.allSockets = new HashMap<UUID, ClientGameSocket>();
  }

  public void moveToSeat(UUID from, Direction to) {
    if (to == null) {
      return;
    }
    ClientGameSocket playerTryingToSeat = this.allSockets.get(from);
    if (playerTryingToSeat == null) {
      return;
    }
    this.moveToSeat(playerTryingToSeat, to);

  }

  public void moveToSeat(ClientGameSocket spectatorGameSocket, Direction direction) {
    LOGGER.info("Entered moveToSeat.");
    ClientGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      LOGGER.info("Trying to seat in an occupied seat. First unsitting player from " + direction.getCompleteName());
      this.unsit(direction);
    }

    if (!spectatorGameSocket.equals(currentSeatedPlayer)) {
      LOGGER.info("Now trying to seat in an empty seat: " + direction.getCompleteName());
      this.sitOnEmptySeat(spectatorGameSocket, direction);
    }

    this.sendDealAll();

    logAllPlayers();
  }

  private void unsit(Direction direction) {
    LOGGER.info("Removing player from " + direction.getCompleteName() + "to spectators");
    ClientGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      currentSeatedPlayer.unsetDirection();
      this.removeFromPlayers(currentSeatedPlayer);
      this.gameServer.getDeal().unsetPlayerOf(direction);
      this.spectatorSockets.add(currentSeatedPlayer);
      this.getSBKingServer().sendIsSpectatorTo(currentSeatedPlayer.getPlayer().getIdentifier());
    }
  }

  private void sitOnEmptySeat(ClientGameSocket clientGameSocket, Direction direction) {
    ClientGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      LOGGER.info("Trying to seat on occupied seat.");
      return;
    }

    if (clientGameSocket.isSpectator()) {
      LOGGER.info("Trying to move from espectators to " + direction.getCompleteName() + ".");
      ClientGameSocket spectatorGameSocket = clientGameSocket;
      spectatorGameSocket.setDirection(direction);
      this.playerSockets.put(direction, spectatorGameSocket);
      this.removeFromSpectators(spectatorGameSocket);
      this.getSBKingServer().sendIsNotSpectatorTo(spectatorGameSocket.getPlayer().getIdentifier());
      this.getSBKingServer().sendDirectionTo(direction, spectatorGameSocket.getPlayer().getIdentifier());
      this.gameServer.getDeal().setPlayerOf(direction, spectatorGameSocket.getPlayer());
    } else {
      LOGGER.info("Trying to move from " + clientGameSocket.getDirection().getCompleteName() + " to "
          + direction.getCompleteName() + ".");
      Direction from = clientGameSocket.getDirection();
      Direction to = direction;

      this.removeFromPlayers(clientGameSocket);
      this.gameServer.getDeal().unsetPlayerOf(from);

      playerSockets.put(to, clientGameSocket);
      clientGameSocket.setDirection(to);
      this.getSBKingServer().sendDirectionTo(to, clientGameSocket.getPlayer().getIdentifier());

      this.gameServer.getDeal().setPlayerOf(to, clientGameSocket.getPlayer());
    }
  }

  private void logAllPlayers() {
    LOGGER.info("--- Logging players ---");
    playerSockets.values().stream().forEach(player -> printPlayerInfo(player));
    spectatorSockets.stream().forEach(player -> printPlayerInfo(player));
    LOGGER.info("--- Finished Logging players ---\n\n\n");
  }

  private void printPlayerInfo(ClientGameSocket playerInfo) {
    String name = playerInfo.getPlayer().getNickname();
    String identifier = playerInfo.getPlayer().getIdentifier().toString();
    Direction direction = playerInfo.getDirection();
    if (playerInfo.isSpectator()) {
      LOGGER.info("SPEC: " + name + "(" + identifier + ")");
    } else {
      LOGGER.info("   " + direction.getAbbreviation() + ": " + name + "(" + identifier + ")");
    }
  }

  private void removeFromSpectators(ClientGameSocket spectatorGameSocket) {
    spectatorSockets.remove(spectatorGameSocket);
  }

  private void removeFromPlayers(ClientGameSocket playerGameSocket) {
    if (playerGameSocket == null) {
      return;
    }
    for (Direction direction : Direction.values()) {
      ClientGameSocket currentPlayerGameSocket = playerSockets.get(direction);
      if (playerGameSocket.equals(currentPlayerGameSocket)) {
        LOGGER.info("Removing player from " + direction.getCompleteName());
        playerSockets.remove(direction);
      }
    }

  }

  public void addSpectator(PlayerNetworkInformation playerNetworkInformation) {
    ClientGameSocket spectatorGameSocket = new ClientGameSocket(playerNetworkInformation, null, this);
    this.spectatorSockets.add(spectatorGameSocket);
    LOGGER.info("Info do spectator:" + spectatorGameSocket);
    this.sendDealAll();
    spectatorGameSocket.setup();
    allSockets.put(playerNetworkInformation.getPlayer().getIdentifier(), spectatorGameSocket);
    logAllPlayers();
  }

  private void removeClientGameSocket(ClientGameSocket playerSocket) {
    this.removeFromPlayers(playerSocket);
    this.removeFromSpectators(playerSocket);
    Direction direction = playerSocket.getDirection();
    if (direction != null) {
      this.gameServer.getDeal().unsetPlayerOf(direction);
      this.sendDealAll();
    }
  }

  public GameServer getGameServer() {
    return gameServer;
  }

  public Player getPlayerOf(Direction direction) {
    ClientGameSocket playerGameSocket = this.playerSockets.get(direction);
    if (playerGameSocket == null) {
      return new Player(UUID.randomUUID(), "Empty seat.");
    } else {
      return playerGameSocket.getPlayer();
    }
  }

  public SBKingServer getSBKingServer() {
    return this.gameServer.getSBKingServer();
  }

  private void sendDealAll() {
    this.getSBKingServer().sendDealAll(this.gameServer.getDeal());
  }

  public void undo(Direction direction) {
    this.gameServer.undo(direction);
    this.sendDealAll();
  }

  public void removePlayer(UUID identifier) {
    ClientGameSocket clientGameSocket = this.allSockets.get(identifier);
    if (clientGameSocket != null) {
      this.removeClientGameSocket(clientGameSocket);
    }
  }

}
