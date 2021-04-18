package br.com.sbk.sbking.dto;

import java.util.Map;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.TrickGame;

public class LobbyScreenTableDTO {

    private final Map<Direction, Player> playersDirection;
    private final TrickGame trickGame;
    private final int numberOfSpectators;

    public LobbyScreenTableDTO(Map<Direction, Player> playersDirection, TrickGame trickGame, int numberOfSpectators) {
        this.playersDirection = playersDirection;
        this.trickGame = trickGame;
        this.numberOfSpectators = numberOfSpectators;
    }

    public Map<Direction, Player> getPlayersDirection() {
        return this.playersDirection;
    }

    public TrickGame getGameScreen() {
        return this.trickGame;
    }

    public int getNumberOfSpectators() {
        return this.numberOfSpectators;
    }

}
