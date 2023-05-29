package br.com.sbk.sbking.networking.client;

import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.listeners.ClientActionListener;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingClient;
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

    private KryonetSBKingClient kryonetSBKingClient;

    private RestHTTPClient restHTTPClient;

    private String gameName;

    private List<LobbyScreenTableDTO> tables;

    private List<String> spectatorNames;

    public void setActionListener(ClientActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setKryonetSBKingClient(KryonetSBKingClient kryonetSBKingClient) {
        this.kryonetSBKingClient = kryonetSBKingClient;
    }

    public void setRestHTTPClient(RestHTTPClient restHTTPClient) {
        this.restHTTPClient = restHTTPClient;
    }

    public void setNickname(String nickname) {
        this.sendSetNickname(nickname);
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
        this.kryonetSBKingClient.sendChooseGameModeOrStrain(gameModeOrStrain);
    }

    public void sendPositive() {
        this.kryonetSBKingClient.sendChoosePositiveMessage();
    }

    public void sendNegative() {
        this.kryonetSBKingClient.sendChooseNegativeMessage();
    }

    public void sendSetNickname(String nickname) {
        this.kryonetSBKingClient.setAndSendNickname(nickname);
    }

    public void sendGetTables() {
        this.kryonetSBKingClient.sendGetTablesMessage();
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

    public void sendCreateTable(String gameName) {
        this.restHTTPClient.sendCreateTableMessage(gameName);
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setTables(List<LobbyScreenTableDTO> tables) {
        this.tables = tables;
    }

    public List<LobbyScreenTableDTO> getTables() {
        return tables;
    }

    public void setSpectatorNames(List<String> spectatorNames) {
        this.spectatorNames = spectatorNames;
    }

    public List<String> getSpectatorNames() {
        return this.spectatorNames;
    }

    public void sendGetSpectatorNames() {
        this.kryonetSBKingClient.sendGetTableSpectators();
    }

    public void initializeId(String id) {
        this.kryonetSBKingClient.setIdentifier(id);
        this.restHTTPClient.setIdentifier(id);
    }

}
