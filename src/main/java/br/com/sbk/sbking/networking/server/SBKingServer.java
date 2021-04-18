package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingServer;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;
import br.com.sbk.sbking.networking.server.gameServer.KingGameServer;
import br.com.sbk.sbking.networking.server.gameServer.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameServer.PositiveKingGameServer;

/**
 * This class has two responsibilities: 1: receiving method calls from the
 * GameServer and sending to client(s) over the network layer
 * (KryonetSBKingServer). 2: receiving method calls from the network layer and
 * act on the GameServer (via Table).
 */
public class SBKingServer {

  private KryonetSBKingServer kryonetSBKingServer = null;

  private Map<UUID, Player> identifierToPlayerMap = new HashMap<UUID, Player>();
  private Map<UUID, Table> tables;
  private Map<Player, Table> playersTable;

  private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 10;
  private ExecutorService pool;

  public SBKingServer() {
    this.tables = new HashMap<UUID, Table>();
    this.playersTable = new HashMap<Player, Table>();
    this.identifierToPlayerMap = new HashMap<UUID, Player>();
    this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
  }

  public void setKryonetSBKingServer(KryonetSBKingServer kryonetSBKingServer) {
    this.kryonetSBKingServer = kryonetSBKingServer;
  }

  public void addUnnammedPlayer(UUID identifier) {
    Player player = new Player(identifier, "SBKingServer Unnamed");
    this.identifierToPlayerMap.put(identifier, player);
  }

  private Direction getDirectionFromIdentifier(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (table != null) {
      return table.getDirectionFrom(player);
    }
    return null;
  }

  public void play(Card card, UUID playerIdentifier) {
    Direction from = this.getDirectionFromIdentifier(playerIdentifier);
    Player player = this.identifierToPlayerMap.get(playerIdentifier);
    Table table = this.playersTable.get(player);
    if (table == null) {
      return;
    }
    table.getGameServer().notifyPlayCard(card, from); // FIXME Law of Demeter
  }

  public void moveToSeat(Direction direction, UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.moveToSeat(player, direction);
  }

  public void choosePositive(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setPositive();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseNegative(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setNegative();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseGameModeOrStrain(String gameModeOrStrainString, UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    GameServer gameServer = table.getGameServer();
    Ruleset gameModeOrStrain = RulesetFromShortDescriptionIdentifier.identify(gameModeOrStrainString);
    Direction directionFromIdentifier = this.getDirectionFromIdentifier(playerIdentifier);
    if (gameModeOrStrain != null && directionFromIdentifier != null) {
      if (gameServer instanceof MinibridgeGameServer) {
        MinibridgeGameServer minibridgeGameServer = (MinibridgeGameServer) table.getGameServer();
        minibridgeGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      } else if (gameServer instanceof KingGameServer) {
        KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
        kingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      } else if (gameServer instanceof PositiveKingGameServer) {
        PositiveKingGameServer positiveKingGameServer = (PositiveKingGameServer) table.getGameServer();
        positiveKingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      }
    }
  }

  public void sendFinishDealAll() {
    this.kryonetSBKingServer.sendFinishDealAll();
  }

  public void sendDirectionTo(Direction direction, UUID playerIdentifier) {
    this.kryonetSBKingServer.sendDirectionTo(direction, playerIdentifier);
  }

  public void sendIsSpectatorTo(UUID playerIdentifier) {
    this.kryonetSBKingServer.sendIsSpectatorTo(playerIdentifier);
  }

  public void sendIsNotSpectatorTo(UUID playerIdentifier) {
    this.kryonetSBKingServer.sendIsNotSpectatorTo(playerIdentifier);
  }

  public void sendDealAll(Deal deal) {
    this.kryonetSBKingServer.sendDealAll(deal);
  }

  public void sendDealTo(Deal deal, UUID playerIdentifier) {
    this.kryonetSBKingServer.sendDealTo(deal, playerIdentifier);
  }

  public void sendGameModeOrStrainChooserAll(Direction direction) {
    this.kryonetSBKingServer.sendGameModeOrStrainChooserAll(direction);
  }

  public void sendPositiveOrNegativeChooserAll(Direction direction) {
    this.kryonetSBKingServer.sendPositiveOrNegativeChooserAll(direction);
  }

  public void sendPositiveOrNegativeAll(PositiveOrNegative positiveOrNegative) {
    String positiveOrNegativeString = positiveOrNegative.toString().toUpperCase();
    this.kryonetSBKingServer.sendPositiveOrNegativeAll(positiveOrNegativeString);
  }

  public void sendInitializeDealAll() {
    this.kryonetSBKingServer.sendInitializeDealAll();
  }

  public void sendInvalidRulesetAll() {
    this.kryonetSBKingServer.sendInvalidRulesetAll();
  }

  public void sendValidRulesetAll() {
    this.kryonetSBKingServer.sendValidRulesetAll();
  }

  public void setNickname(UUID identifier, String nickname) {
    Player player = identifierToPlayerMap.get(identifier);
    player.setNickname(nickname);
  }

  public String getNicknameFromIdentifier(UUID identifier) {
    Player player = identifierToPlayerMap.get(identifier);
    if (player != null) {
      return player.getNickname();
    } else {
      return null;
    }
  }

  public boolean nobodyIsConnected() {
    return this.kryonetSBKingServer.nobodyIsConnected();
  }

  public void addSpectator(UUID playerIdentifier, UUID tableIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table currentTable = playersTable.get(player);
    Table table = this.tables.get(tableIdentifier);
    if (player == null || table == null) {
      return;
    }
    if (currentTable != null) {
      currentTable.removePlayer(playerIdentifier);
    }
    table.addSpectator(player);
    table.sendDealTo(player);
  }

  public void undo(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.undo(player);
  }

  public void removePlayer(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }

    table.removePlayer(playerIdentifier);
    identifierToPlayerMap.remove(playerIdentifier);
    playersTable.remove(player);
  }

  public Table getTable(UUID tableIdentifier) {
    return this.tables.get(tableIdentifier);
  }

  public void createTable(Class<? extends GameServer> gameServerClass) {
    GameServer gameServer;
    try {
      gameServer = gameServerClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      LOGGER.fatal("Could not initialize GameServer with received gameServerClass.");
      return;
    }
    Table table = new Table(gameServer);
    gameServer.setSBKingServer(this);
    tables.put(table.getId(), table);
    pool.execute(gameServer);
    LOGGER.info("Created new table and executed its gameServer!");
  }

}
