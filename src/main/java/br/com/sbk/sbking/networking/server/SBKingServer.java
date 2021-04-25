package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingServer;
import br.com.sbk.sbking.networking.kryonet.messages.GameNameFromGameServerIdentifier;
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

  public void sendFinishDealToTable(Table table) {
    this.kryonetSBKingServer.sendFinishDealTo(this.getAllPlayersUUIDsOnTable(table));
  }

  private Iterable<UUID> getAllPlayersUUIDsOnTable(Table table) {
    Collection<UUID> allPlayersOnTable = new LinkedList<UUID>();
    if (table == null) {
      return allPlayersOnTable;
    }
    for (Map.Entry<Player, Table> pair : playersTable.entrySet()) {
      if (table.equals(pair.getValue())) {
        allPlayersOnTable.add(pair.getKey().getIdentifier());
      }
    }
    return allPlayersOnTable;
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

  public void sendDealToTable(Deal deal, Table table) {
    this.kryonetSBKingServer.sendDealTo(deal, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendDealTo(Deal deal, UUID playerIdentifier) {
    this.kryonetSBKingServer.sendDealTo(deal, playerIdentifier);
  }

  public void sendGameModeOrStrainChooserToTable(Direction direction, Table table) {
    this.kryonetSBKingServer.sendGameModeOrStrainChooserTo(direction, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendPositiveOrNegativeChooserToTable(Direction direction, Table table) {
    this.kryonetSBKingServer.sendPositiveOrNegativeChooserTo(direction, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendPositiveOrNegativeToTable(PositiveOrNegative positiveOrNegative, Table table) {
    String positiveOrNegativeString = positiveOrNegative.toString().toUpperCase();
    this.kryonetSBKingServer.sendPositiveOrNegativeTo(positiveOrNegativeString, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendInitializeDealToTable(Table table) {
    this.kryonetSBKingServer.sendInitializeDealTo(this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendInvalidRulesetToTable(Table table) {
    this.kryonetSBKingServer.sendInvalidRulesetTo(this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendValidRulesetToTable(Table table) {
    this.kryonetSBKingServer.sendValidRulesetTo(this.getAllPlayersUUIDsOnTable(table));
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

  public void addSpectator(Player player, Table table) {
    Table currentTable = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    if (currentTable != null) {
      currentTable.removePlayer(player.getIdentifier());
    }
    table.addSpectator(player);
    table.sendDealTo(player);
  }

  public void joinTable(UUID playerIdentifier, UUID tableIdentifier) {
    Table table = this.tables.get(tableIdentifier);
    Player player = this.identifierToPlayerMap.get(playerIdentifier);
    if (table != null && player != null) {
      GameServer gameServer = table.getGameServer();
      String gameName = GameNameFromGameServerIdentifier.identify(gameServer.getClass());
      this.kryonetSBKingServer.sendYourTableIsTo(gameName, playerIdentifier);
      this.addSpectator(player, table);
      this.playersTable.put(player, table);
    }
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
    this.leaveTable(playerIdentifier);
    identifierToPlayerMap.remove(playerIdentifier);
  }

  public Table getTable(UUID tableIdentifier) {
    return this.tables.get(tableIdentifier);
  }

  public void createTable(Class<? extends GameServer> gameServerClass, UUID playerIdentifier) {
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

    this.joinTable(playerIdentifier, table.getId());
  }

  public void sendTablesTo(UUID playerIdentifier) {
    this.kryonetSBKingServer.sendTablesTo(createTablesDTO(), playerIdentifier);
  }

  public void sendTablesToAll() {
    this.kryonetSBKingServer.sendTablesToAll(createTablesDTO());
  }

  private List<LobbyScreenTableDTO> createTablesDTO() {
    return this.tables.values().stream().map(LobbyScreenTableDTO::new).collect(Collectors.toList());
  }

  public void leaveTable(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }

    table.removePlayer(playerIdentifier);
    playersTable.remove(player);
    if (table.isEmpty()) {
      this.tables.remove(table.getId());
    }
    this.kryonetSBKingServer.sendYourTableIsTo(null, playerIdentifier);

  }

}
