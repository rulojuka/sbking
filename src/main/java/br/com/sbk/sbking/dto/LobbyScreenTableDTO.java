package br.com.sbk.sbking.dto;

import java.util.Map;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.screens.GameScreen;

public class LobbyScreenTableDTO {

    private final Map<Direction, Player> playersDirection;
    private final GameScreen gameScreen;
    private final int numberOfSpectators;

    public LobbyScreenTableDTO(Map<Direction, Player> playersDirection, GameScreen gameScreen, int numberOfSpectators) {
        this.playersDirection = playersDirection;
        this.gameScreen = gameScreen;
        this.numberOfSpectators = numberOfSpectators;
    }

    public Map<Direction, Player> getPlayersDirection() {
        return this.playersDirection;
    }

    public GameScreen getGameScreen() {
        return this.gameScreen;
    }

    public int getNumberOfSpectators() {
        return this.numberOfSpectators;
    }

}
