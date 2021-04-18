package br.com.sbk.sbking.dto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.screens.GameScreen;

public class LobbyScreenTableDTOTest {

    @Test
    public void getPlayersDirectionShouldReturnPlayersDirection() {
        Map<Direction, Player> playersDirection = new HashMap<Direction, Player>();
        GameScreen gameScreen = mock(GameScreen.class);
        int numberOfSpectators = 1;
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, gameScreen, numberOfSpectators);

        Map<Direction, Player> dtoPlayerDirection = dto.getPlayersDirection();

        assertEquals(playersDirection, dtoPlayerDirection);
    }

    @Test
    public void getNumberOfSpectatorsShouldReturnNumberOfSpectators() {
        Map<Direction, Player> playersDirection = new HashMap<Direction, Player>();
        GameScreen gameScreen = mock(GameScreen.class);
        int numberOfSpectators = 1;
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, gameScreen, numberOfSpectators);

        int dtoNumberOfSpectators = dto.getNumberOfSpectators();

        assertEquals(numberOfSpectators, dtoNumberOfSpectators);
    }

    @Test
    public void getGameScreenShouldReturnTheGameScreen() {
        Map<Direction, Player> playersDirection = new HashMap<Direction, Player>();
        GameScreen gameScreen = mock(GameScreen.class);
        int numberOfSpectators = 1;
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, gameScreen, numberOfSpectators);

        GameScreen dtoGameScreen = dto.getGameScreen();

        assertEquals(gameScreen, dtoGameScreen);
    }

}
