package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.kryonet.IdentifierToPlayerMap;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingServer;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;
import br.com.sbk.sbking.networking.server.gameServer.KingGameServer;
import br.com.sbk.sbking.networking.server.gameServer.MinibridgeGameServer;

/**
 * This class has two responsibilities: 1: receiving method calls from the
 * GameServer and sending to client(s) over the network layer
 * (KryonetSBKingServer). 2: receiving method calls from the network layer and
 * act on the GameServer (via Table).
 */
public class SBKingServer {

  IdentifierToPlayerMap identifierToPlayerMap;
  KryonetSBKingServer kryonetSBKingServer = null;
  private Table table;
  private Map<UUID, Direction> playerDirections = new HashMap<UUID, Direction>();

  public SBKingServer(Table table) {
    this.table = table;
    this.identifierToPlayerMap = new IdentifierToPlayerMap();
  }

  public void setKryonetSBKingServer(KryonetSBKingServer kryonetSBKingServer) {
    this.kryonetSBKingServer = kryonetSBKingServer;
  }

  public void addUnnammedPlayer(UUID identifier) {
    Player player = new Player(identifier, "SBKingServer Unnamed");
    this.identifierToPlayerMap.put(identifier, player);
  }

  private Direction getDirectionFromIdentifier(UUID playerIdentifier) {
    return playerDirections.get(playerIdentifier);
  }

  public void play(Card card, UUID playerIdentifier) {
    Direction from = this.getDirectionFromIdentifier(playerIdentifier);
    this.table.getGameServer().notifyPlayCard(card, from); // FIXME Law of Demeter
  }

  public void moveToSeat(Direction direction, UUID playerIdentifier) {
    Direction from = this.getDirectionFromIdentifier(playerIdentifier);
    Direction to = direction;
    LOGGER.debug("Moving from: " + from + "  to: " + to);
    this.table.moveToSeat(playerIdentifier, to);
    // Should also remove the previously seated player from the map
    if (from != null && from.equals(to)) {
      playerDirections.remove(playerIdentifier);
    } else {
      playerDirections.put(playerIdentifier, to);
    }
  }

  public void choosePositive(UUID playerIdentifier) {
    KingGameServer kingGameServer = (KingGameServer) this.table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setPositive();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseNegative(UUID playerIdentifier) {
    KingGameServer kingGameServer = (KingGameServer) this.table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setNegative();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseGameModeOrStrain(String gameModeOrStrainString, UUID playerIdentifier) {
    GameServer gameServer = this.table.getGameServer();
    Ruleset gameModeOrStrain = RulesetFromShortDescriptionIdentifier.identify(gameModeOrStrainString);
    Direction directionFromIdentifier = this.getDirectionFromIdentifier(playerIdentifier);
    if (gameModeOrStrain != null && directionFromIdentifier != null) {
      if (gameServer instanceof MinibridgeGameServer) {
        MinibridgeGameServer minibridgeGameServer = (MinibridgeGameServer) this.table.getGameServer();
        minibridgeGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      } else if (gameServer instanceof KingGameServer) {
        KingGameServer kingGameServer = (KingGameServer) this.table.getGameServer();
        kingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
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

  public void sendBoardAll(Board board) {
    this.kryonetSBKingServer.sendBoardAll(board);
  }

  public void sendDealAll(Deal deal) {
    this.kryonetSBKingServer.sendDealAll(deal);
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

  public void addSpectator(UUID identifier) {
    this.table.addSpectator(new PlayerNetworkInformation(this.identifierToPlayerMap.get(identifier)));
  }

}
