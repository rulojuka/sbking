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

/**
 * This class has two responsibilities: 1: control the players and the
 * directions they are sitting 2: host a gameServer. This way, the gameServer
 * should remain isolated from the lower layers and only be altered via this
 * table.
 */
public class Table {

  private Map<Direction, Player> seatedPlayers;
  private Collection<Player> spectatorPlayers;
  private Map<UUID, Player> allPlayers;
  private GameServer gameServer;
  private UUID id;

  public Table(GameServer gameServer) {
    this.gameServer = gameServer;
    this.gameServer.setTable(this);
    this.seatedPlayers = new HashMap<Direction, Player>();
    this.spectatorPlayers = new ArrayList<Player>();
    this.allPlayers = new HashMap<UUID, Player>();
    this.id = UUID.randomUUID();
  }

  public void moveToSeat(UUID playerIdentifier, Direction to) {
    if (to == null) {
      return;
    }
    Player playerTryingToSeat = this.allPlayers.get(playerIdentifier);
    if (playerTryingToSeat == null) {
      return;
    }
    this.moveToSeat(playerTryingToSeat, to);

  }

  public void moveToSeat(Player playerTryingToSeat, Direction direction) {
    LOGGER.debug("Entered moveToSeat.");
    Player currentSeatedPlayer = this.seatedPlayers.get(direction);
    if (currentSeatedPlayer != null) {
      LOGGER.debug("Trying to seat in an occupied seat. First unsitting player from " + direction.getCompleteName());
      this.unsit(direction);
    }

    if (!playerTryingToSeat.equals(currentSeatedPlayer)) {
      LOGGER.debug("Now trying to seat in an empty seat: " + direction.getCompleteName());
      this.sitOnEmptySeat(playerTryingToSeat, direction);
    }

    this.sendDealAll();

    logAllPlayers();
  }

  private void unsit(Direction direction) {
    LOGGER.debug("Removing player from " + direction.getCompleteName() + "to spectators");
    Player currentSeatedPlayer = this.seatedPlayers.get(direction);
    if (currentSeatedPlayer != null) {
      this.removeFromSeatedPlayers(currentSeatedPlayer);
      this.gameServer.getDeal().unsetPlayerOf(direction);
      this.spectatorPlayers.add(currentSeatedPlayer);
      this.getSBKingServer().sendIsSpectatorTo(currentSeatedPlayer.getIdentifier());
    }
  }

  private void sitOnEmptySeat(Player player, Direction direction) {
    Player currentSeatedPlayer = this.seatedPlayers.get(direction);
    if (currentSeatedPlayer != null) {
      LOGGER.info("Trying to seat on occupied seat.");
      return;
    }

    if (this.isSpectator(player)) {
      LOGGER.debug("Trying to move from espectators to " + direction.getCompleteName() + ".");
      this.seatedPlayers.put(direction, player);
      this.removeFromSpectators(player);
      this.getSBKingServer().sendIsNotSpectatorTo(player.getIdentifier());
      this.getSBKingServer().sendDirectionTo(direction, player.getIdentifier());
      this.gameServer.getDeal().setPlayerOf(direction, player);
    } else {
      Direction from = this.getDirectionFrom(player);
      if (from == null) {
        return;
      }
      Direction to = direction;
      LOGGER.debug("Trying to move from " + from.getCompleteName() + " to " + to.getCompleteName() + ".");

      this.removeFromSeatedPlayers(player);
      this.gameServer.getDeal().unsetPlayerOf(from);

      seatedPlayers.put(to, player);
      this.getSBKingServer().sendDirectionTo(to, player.getIdentifier());

      this.gameServer.getDeal().setPlayerOf(to, player);
    }
  }

  private boolean isSpectator(Player player) {
    return this.spectatorPlayers.contains(player);
  }

  private void logAllPlayers() {
    LOGGER.info("\n\n\n--- Logging players ---");
    seatedPlayers.values().stream().forEach(this::printPlayerInfo);
    spectatorPlayers.stream().forEach(this::printPlayerInfo);
    LOGGER.info("--- Finished Logging players ---\n\n\n");
  }

  private void printPlayerInfo(Player player) {
    String name = player.getNickname();
    String identifier = player.getIdentifier().toString();
    Direction direction = this.getDirectionFrom(player);
    if (direction == null) {
      LOGGER.info("SPEC: " + name + "(" + identifier + ")");
    } else {
      LOGGER.info("   " + direction.getAbbreviation() + ": " + name + "(" + identifier + ")");
    }
  }

  private void removeFromSpectators(Player player) {
    spectatorPlayers.remove(player);
  }

  private void removeFromSeatedPlayers(Player player) {
    if (player == null) {
      return;
    }
    Direction playerDirection = getDirectionFrom(player);
    if (playerDirection != null) {
      LOGGER.debug("Removing player from " + playerDirection.getCompleteName());
      seatedPlayers.remove(playerDirection);
    }
  }

  public void addSpectator(Player player) {
    this.spectatorPlayers.add(player);

    UUID identifier = player.getIdentifier();
    this.getSBKingServer().sendIsSpectatorTo(identifier);
    allPlayers.put(identifier, player);

    logAllPlayers();
  }

  private void removePlayer(Player player) {
    this.removeFromSeatedPlayers(player);
    this.removeFromSpectators(player);
    Direction direction = getDirectionFrom(player);
    if (direction != null) {
      this.gameServer.getDeal().unsetPlayerOf(direction);
      this.sendDealAll();
    }
  }

  public GameServer getGameServer() {
    return gameServer;
  }

  public Player getPlayerOf(Direction direction) {
    Player player = this.seatedPlayers.get(direction);
    if (player == null) {
      return new Player(UUID.randomUUID(), "Empty seat.");
    } else {
      return player;
    }
  }

  public SBKingServer getSBKingServer() {
    return this.gameServer.getSBKingServer();
  }

  public void sendDealAll() {
    this.getSBKingServer().sendDealAll(this.gameServer.getDeal());
  }

  public void sendDealTo(Player player) {
    this.getSBKingServer().sendDealTo(this.gameServer.getDeal(), player.getIdentifier());
  }

  public void undo(Direction direction) {
    this.gameServer.undo(direction);
    this.sendDealAll();
  }

  public void undo(Player player) {
    Direction directionFromPlayer = this.getDirectionFrom(player);
    if (directionFromPlayer != null) {
      this.undo(directionFromPlayer);
    }
  }

  public void removePlayer(UUID identifier) {
    Player player = this.allPlayers.get(identifier);
    if (player != null) {
      this.removePlayer(player);
    }
  }

  public Direction getDirectionFrom(Player player) {
    for (Direction direction : Direction.values()) {
      Player currentPlayer = seatedPlayers.get(direction);
      if (player.equals(currentPlayer)) {
        return direction;
      }
    }
    return null;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Table)) {
      return false;
    }
    Table otherTable = (Table) obj;
    return this.getId().equals(otherTable.getId());
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }

}
