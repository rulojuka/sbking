package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Deque;

import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class MinibridgeGameTest {

    @Test
    public void getLeaderShouldReturnTheNextDirectionFromDealer() {
        Deque<Card> deck = mock(Deque.class);
        MinibridgeGame minibridgeGame = new MinibridgeGame(deck);
        Direction dealer = minibridgeGame.getDealer();
        assertEquals(dealer.next(), minibridgeGame.getLeader());
    }

}
