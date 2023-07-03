package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;

public class BoardTest {

    private Direction dealer;
    private Board board;
    private Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);

    @BeforeEach
    public void createNorthBoard() {
        dealer = Direction.NORTH;
        hands.clear();
        for (Direction direction : Direction.values()) {
            hands.put(direction, mock(Hand.class));
        }

        board = new Board(hands, dealer);
    }

    @Test
    public void shouldBeConstructedWith4HandsAndADealer() {
        assertNotNull(this.board);
    }

    @Test
    public void shouldSortAllHands() {
        Comparator<Card> comparator = mock(CardInsideHandComparator.class);
        for (Direction direction : Direction.values()) {
            Hand currentMock = this.hands.get(direction);
            // To clear constructor calls to sort
            reset(currentMock);
        }

        this.board.sortAllHands(comparator);

        for (Direction direction : Direction.values()) {
            Hand currentMock = this.hands.get(direction);
            verify(currentMock, only()).sort(comparator);
        }
    }

    @Test
    public void shouldGetCorrectDealer() {
        assertEquals(this.dealer, this.board.getDealer());
    }

    @Test
    public void shouldGetHandOfAllPossibleDirections() {
        for (Direction direction : Direction.values()) {
            assertEquals(this.hands.get(direction), this.board.getHandOf(direction));
        }
    }

}
