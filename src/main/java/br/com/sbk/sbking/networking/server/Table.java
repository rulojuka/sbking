package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;

public class Table {

  private ExecutorService pool;
  private static final int MAXIMUM_NUMBER_OF_PLAYERS_AND_KIBITZERS_IN_A_TABLE = 30;

  private Map<Direction, ClientGameSocket> playerSockets = new HashMap<Direction, ClientGameSocket>();
  private Collection<ClientGameSocket> spectatorSockets = new ArrayList<ClientGameSocket>();
  private PlayerNetworkInformation owner;
  private MessageSender messageSender;
  private GameServer gameServer;

  public Table(PlayerNetworkInformation owner, GameServer gameServer) {
    this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_PLAYERS_AND_KIBITZERS_IN_A_TABLE);
    this.messageSender = new MessageSender();
    this.owner = owner;
    this.gameServer = gameServer;
    this.addSpectator(owner);
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

    this.messageSender.sendDealAll(this.gameServer.getDeal());

    logAllSockets();
  }

  private void unsit(Direction direction) {
    LOGGER.info("Removing player from " + direction.getCompleteName() + "to spectators");
    ClientGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      currentSeatedPlayer.unsetDirection();
      this.removeFromPlayers(currentSeatedPlayer);
      this.gameServer.getDeal().unsetPlayerOf(direction);
      this.spectatorSockets.add(currentSeatedPlayer);
      currentSeatedPlayer.sendIsSpectator();
    }
  }

  private void sitOnEmptySeat(ClientGameSocket clientGameSocket, Direction direction) {
    ClientGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      LOGGER.error("Trying to seat on occupied seat.");
      return;
    }

    if (clientGameSocket.isSpectator()) {
      LOGGER.info("Trying to move from espectators to " + direction.getCompleteName() + ".");
      ClientGameSocket spectatorGameSocket = clientGameSocket;
      spectatorGameSocket.setDirection(direction);
      this.playerSockets.put(direction, spectatorGameSocket);
      this.removeFromSpectators(spectatorGameSocket);
      spectatorGameSocket.sendIsNotSpectator();
      spectatorGameSocket.sendDirection(direction);
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
      clientGameSocket.sendDirection(to);

      this.gameServer.getDeal().setPlayerOf(to, clientGameSocket.getPlayer());
    }
  }

  private void logAllSockets() {
    LOGGER.info("--- Logging sockets ---");
    playerSockets.values().stream().forEach(socket -> printSocket(socket));
    spectatorSockets.stream().forEach(socket -> printSocket(socket));
    LOGGER.info("--- Finished Logging sockets ---\n\n\n");
  }

  private void printSocket(ClientGameSocket socket) {
    String name = socket.getPlayer().getName();
    Direction direction = socket.getDirection();
    if (socket.isSpectator()) {
      LOGGER.info("SPEC: " + name);
    } else {
      LOGGER.info("   " + direction.getAbbreviation() + ": " + name);
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
    this.messageSender.addClientGameSocket(spectatorGameSocket);
    LOGGER.info("Info do spectator:" + spectatorGameSocket);
    this.messageSender.sendDealAll(this.gameServer.getDeal());
    pool.execute(spectatorGameSocket);

    logAllSockets();
  }

  public void removeClientGameSocket(ClientGameSocket playerSocket) {
    this.messageSender.removeClientGameSocket(playerSocket);
    for (Direction direction : Direction.values()) {
      ClientGameSocket current = this.playerSockets.get(direction);
      if (playerSocket.equals(current)) {
        this.playerSockets.remove(direction);
      }
    }
    this.spectatorSockets.remove(playerSocket);
    if (playerSocket.getSocket().equals(owner.getSocket())) {
      LOGGER.info("Removing owner! Something bad happened!");
    }
  }

  public GameServer getGameServer() {
    return gameServer;
  }

  public MessageSender getMessageSender() {
    return messageSender;
  }

  public Player getPlayerOf(Direction direction) {
    ClientGameSocket playerGameSocket = this.playerSockets.get(direction);
    if (playerGameSocket == null) {
      return new Player("Empty seat.");
    } else {
      return playerGameSocket.getPlayer();
    }
  }
}
