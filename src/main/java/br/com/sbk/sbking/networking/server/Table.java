package br.com.sbk.sbking.networking.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;

public class Table {

  final static Logger logger = LogManager.getLogger(Table.class);

  private ExecutorService pool;
  private static final int MAXIMUM_NUMBER_OF_PLAYERS_AND_KIBITZERS_IN_A_TABLE = 20;

  private Map<Direction, PlayerGameSocket> playerSockets = new HashMap<Direction, PlayerGameSocket>();
  private Collection<SpectatorGameSocket> spectatorSockets = new ArrayList<SpectatorGameSocket>();
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

  // private Collection<ClientGameSocket> getAllSockets() {
  // Collection<ClientGameSocket> allSockets = new ArrayList<ClientGameSocket>();
  // allSockets.addAll(playerSockets.values());
  // allSockets.addAll(spectatorSockets);
  // return allSockets;
  // }

  public void addPlayer(PlayerNetworkInformation playerNetworkInformation, Direction direction) {
    PlayerGameSocket currentSeatedPlayer = this.playerSockets.get(direction);
    if (currentSeatedPlayer != null) {
      logger.info("Trying to seat in an occupied seat. Ignoring request.");
    } else {
      PlayerGameSocket currentPlayerGameSocket = new PlayerGameSocket(playerNetworkInformation, direction, this);
      this.playerSockets.put(direction, currentPlayerGameSocket);
      pool.execute(currentPlayerGameSocket);
    }
  }

  public void addSpectator(PlayerNetworkInformation playerNetworkInformation) {
    SpectatorGameSocket spectatorGameSocket = new SpectatorGameSocket(playerNetworkInformation, this);
    this.spectatorSockets.add(spectatorGameSocket);
    this.messageSender.addClientGameSocket(spectatorGameSocket);
    logger.info("Info do spectator:" + spectatorGameSocket);
    this.messageSender.sendBoardAll(this.gameServer.getBoard());
    pool.execute(spectatorGameSocket);
  }

  public void removeClientGameSocket(ClientGameSocket playerSocket) {
    for (Direction direction : Direction.values()) {
      PlayerGameSocket current = this.playerSockets.get(direction);
      if (playerSocket.equals(current)) {
        this.playerSockets.remove(direction);
      }
    }
    this.spectatorSockets.remove(playerSocket);
    if (playerSocket.getSocket().equals(owner.getSocket())) {
      logger.info("Removing owner! Something bad happened!");
    }
  }

  public GameServer getGameServer() {
    return gameServer;
  }

  public MessageSender getMessageSender() {
    return messageSender;
  }

  public Player getPlayerOf(Direction direction) {
    PlayerGameSocket playerGameSocket = this.playerSockets.get(direction);
    if (playerGameSocket == null) {
      return new Player("Empty seat.");
    } else {
      return playerGameSocket.getPlayer();
    }
  }
}