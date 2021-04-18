package br.com.sbk.sbking.dto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.networking.server.Table;

public class LobbyScreenTableDTOTest {

    @Mock
    private UUID tableId;
    @Mock
    private Map<Direction, Player> playersDirections;
    @Mock
    private String gameName;
    @Mock
    private int numberOfSpectators;

    LobbyScreenTableDTO subject;

    @Before
    public void setupMocks() {
        Table table = mock(Table.class);
        when(table.getId()).thenReturn(tableId);
        when(table.getPlayersDirections()).thenReturn(playersDirections);
        when(table.getGameName()).thenReturn(gameName);
        when(table.getNumberOfSpectators()).thenReturn(numberOfSpectators);
        subject = new LobbyScreenTableDTO(table);
    }

    @Test
    public void getIdDirectionShouldReturnTableId() {
        assertEquals(tableId, subject.getId());
    }

    @Test
    public void getPlayersDirectionShouldReturnPlayersDirection() {
        assertEquals(playersDirections, subject.getPlayersDirection());
    }

    @Test
    public void getNumberOfSpectatorsShouldReturnNumberOfSpectators() {
        assertEquals(numberOfSpectators, subject.getNumberOfSpectators());
    }

    @Test
    public void getGameNameShouldReturnTheGameName() {
        assertEquals(gameName, subject.getGameName());
    }

}
