package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Hand;

public class OpeningTrainerGameTest {

    @Test
    public void getOpeningShouldReturnCorrectOpening() {

        Board boardWithTwoClubs = this.boardWithTwoClubs();

        OpeningTrainerGame openingTrainerTwoClubs = new OpeningTrainerGame(boardWithTwoClubs);
        String twoClubs = openingTrainerTwoClubs.getOpening();

        assertEquals("2c", twoClubs);
    }

    private Board boardWithTwoClubs() {
        List<Card> cards = new Arrays.asList();
        Hand hand = mock(Hand.class);
        when(hand.getCards()).thenReturn(cards);
        Board board = mock(Board.class);
        when(board.getHandOf(any())).thenReturn(hand);
        return board;
    }

}
