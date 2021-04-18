package br.com.sbk.sbking.dto;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.TrickGame;

public class LobbyScreenTableDTOTest {

    @Mock
    Map<Direction, Player> playersDirection;
    @Mock
    TrickGame trickGame;
    @Mock
    int numberOfSpectators;

    @Test
    public void getPlayersDirectionShouldReturnPlayersDirection() {
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, trickGame, numberOfSpectators);

        assertEquals(playersDirection, dto.getPlayersDirection());
    }

    @Test
    public void getNumberOfSpectatorsShouldReturnNumberOfSpectators() {
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, trickGame, numberOfSpectators);

        assertEquals(numberOfSpectators, dto.getNumberOfSpectators());
    }

    @Test
    public void getGameScreenShouldReturnTheGameScreen() {
        LobbyScreenTableDTO dto = new LobbyScreenTableDTO(playersDirection, trickGame, numberOfSpectators);

        assertEquals(trickGame, dto.getGameScreen());
    }

}
