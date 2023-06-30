package br.com.sbk.sbking.networking.client;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sbk.sbking.clientapp.DealFrameHandler;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.listeners.ClientActionListener;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.rest.RestHTTPClient;

public class SBKingClient {

    private Direction direction;

    private Direction positiveOrNegativeChooser;
    private Direction gameModeOrStrainChooser;
    private PositiveOrNegative positiveOrNegative;

    private Deal currentDeal;
    private boolean dealHasChanged = true;

    private Boolean rulesetValid = null;

    private KingGameScoreboard currentGameScoreboard = new KingGameScoreboard();

    private ClientActionListener actionListener;

    private boolean spectator;

    private RestHTTPClient restHTTPClient;

    private String gameName = null;

    private List<LobbyScreenTableDTO> tables;

    private List<String> spectatorNames;

    private String nickname;

    private boolean shouldRedrawTables = true;

    private UUID identifier;

    private UUID currentTable = null;

    private StompSession stompSession;

    private Subscription dealSubscription;

    public void setStompSession(StompSession stompSession) {
        this.stompSession = stompSession;
    }

    public void setCurrentTable(UUID newTable) {
        if (this.currentTable != null && newTable == null) {
            LOGGER.info("Unsubscribing!");
            this.dealSubscription.unsubscribe(); // dealSubscription should not be null here
            this.dealSubscription = null;
        }
        if (newTable != null && !newTable.equals(this.currentTable)) {
            String topic = "/topic/deal/" + newTable.toString();
            LOGGER.info("Subscribing to: {}", topic);
            this.dealSubscription = this.stompSession.subscribe(topic, new DealFrameHandler(this));
            this.restHTTPClient.refreshTable(newTable);
        }
        this.currentTable = newTable;
    }

    public void setActionListener(ClientActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setRestHTTPClient(RestHTTPClient restHTTPClient) {
        this.restHTTPClient = restHTTPClient;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isNicknameSet() {
        return this.nickname != null;
    }

    public void initializeDirection(Direction direction) {
        this.direction = direction;
    }

    public void finishDeal() {
        this.initializeEverythingToNextDeal();
    }

    public void initializeDeal() {
        this.initializeEverythingToNextDeal();
    }

    private void initializeEverythingToNextDeal() {
        this.unsetPositiveOrNegativeChooser();
        this.unsetPositiveOrNegative();
        this.unsetGameModeOrStrainChooser();
        this.unsetCurrentDeal();
        this.unsetRulesetValid();
    }

    public void resetBeforeEnteringTable() {
        this.unsetPositiveOrNegativeChooser();
        this.unsetPositiveOrNegative();
        this.unsetGameModeOrStrainChooser();
        this.unsetRulesetValid();
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isDirectionOrSpectatorSet() {
        return this.direction != null || this.spectator;
    }

    public void setPositiveOrNegativeChooser(Direction direction) {
        this.positiveOrNegativeChooser = direction;
    }

    public boolean isPositiveOrNegativeChooserSet() {
        return this.positiveOrNegativeChooser != null;
    }

    private void unsetPositiveOrNegativeChooser() {
        this.positiveOrNegativeChooser = null;
    }

    public Direction getPositiveOrNegativeChooser() {
        return this.positiveOrNegativeChooser;
    }

    public void setGameModeOrStrainChooser(Direction direction) {
        this.gameModeOrStrainChooser = direction;
    }

    private void unsetGameModeOrStrainChooser() {
        this.gameModeOrStrainChooser = null;
    }

    public boolean isGameModeOrStrainChooserSet() {
        return this.gameModeOrStrainChooser != null;
    }

    public Direction getGameModeOrStrainChooser() {
        return this.gameModeOrStrainChooser;
    }

    public void selectedPositive() {
        this.positiveOrNegative = new PositiveOrNegative();
        this.positiveOrNegative.setPositive();
    }

    public void selectedNegative() {
        this.positiveOrNegative = new PositiveOrNegative();
        this.positiveOrNegative.setNegative();
    }

    public boolean isPositiveOrNegativeSelected() {
        if (this.positiveOrNegative == null) {
            return false;
        }
        return this.positiveOrNegative.isSelected();
    }

    public boolean isPositive() {
        return this.positiveOrNegative.isPositive();
    }

    private void unsetPositiveOrNegative() {
        this.positiveOrNegative = null;
    }

    public void setRulesetValid(boolean valid) {
        this.rulesetValid = valid;
    }

    private void unsetRulesetValid() {
        this.rulesetValid = null;
    }

    public void setCurrentDeal(Deal deal) {
        this.currentDeal = deal;
        this.dealHasChanged = true;
    }

    private void unsetCurrentDeal() {
        this.currentDeal = null;
    }

    public Deal getDeal() {
        this.dealHasChanged = false;
        return this.currentDeal;
    }

    public boolean getDealHasChanged() {
        return this.dealHasChanged;
    }

    public boolean isRulesetValidSet() {
        return this.rulesetValid != null;
    }

    public ClientActionListener getActionListener() {
        return this.actionListener;
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public KingGameScoreboard getCurrentGameScoreboard() {
        return this.currentGameScoreboard;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public void sendChooseGameModeOrStrain(String gameModeOrStrain) {
        this.restHTTPClient.chooseGameModeOrStrain(gameModeOrStrain);
    }

    public void sendPositive() {
        this.restHTTPClient.choosePositive();
    }

    public void sendNegative() {
        this.restHTTPClient.chooseNegative();
    }

    public void sendNickname() {
        this.restHTTPClient.sendNickname(this.nickname);
    }

    public void sendGetTables() {
        List<LobbyScreenTableDTO> tables = this.restHTTPClient.getTables();
        if (tables == null) {
            this.initializeTables();
        } else {
            this.setTables(tables);
        }
    }

    public void setPositiveOrNegative(String content) {
        String positive = "POSITIVE";
        String negative = "NEGATIVE";
        if (positive.equals(content)) {
            this.selectedPositive();
        } else if (negative.equals(content)) {
            this.selectedNegative();
        } else {
            throw new IllegalArgumentException("Content must be POSITIVE or NEGATIVE");
        }
    }

    public UUID sendCreateTable(String gameName) {
        String response = this.restHTTPClient.sendCreateTableMessage(gameName);
        ObjectMapper objectMapper = new ObjectMapper();
        UUID tableId = null;
        try {
            tableId = objectMapper.readValue(response, UUID.class);
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        return tableId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setTables(List<LobbyScreenTableDTO> tables) {
        if (tables.size() != this.tables.size()) {
            // This is a bad attempt at avoiding redraws when the tables don't change.
            // It should be done in the drawing, not here.
            this.tables = tables;
            this.shouldRedrawTables = true;
        }
    }

    public List<LobbyScreenTableDTO> getTables() {
        if (this.tables == null) {
            this.initializeTables();
        }
        this.shouldRedrawTables = false;
        return tables;
    }

    public void initializeTables() {
        this.tables = new ArrayList<LobbyScreenTableDTO>();
        this.shouldRedrawTables = true;
    }

    public boolean shouldRedrawTables() {
        return this.shouldRedrawTables;
    }

    public void setSpectatorNames(List<String> spectatorNames) {
        this.spectatorNames = spectatorNames;
    }

    public List<String> getSpectatorNames() {
        return this.spectatorNames;
    }

    public void sendGetSpectatorNames() {
        List<String> spectators = this.restHTTPClient.getSpectators();
        this.setSpectatorNames(spectators);
    }

    public void initializeId(String id) {
        this.restHTTPClient.setIdentifier(id);
        this.identifier = UUID.fromString(id);
    }

    public UUID getId() {
        return this.identifier;
    }

    public void sendJoinTable(UUID tableId) {
        this.restHTTPClient.sendJoinTableMessage(tableId);
    }

}
