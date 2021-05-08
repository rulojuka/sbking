package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.sbk.sbking.core.Direction;

public class MinibridgeGameTest {

    @Test
    public void getLeaderShouldReturnTheNextDirectionFromDealer() {
        MinibridgeGame minibridgeGame = new MinibridgeGame();
        Direction dealer = minibridgeGame.getDealer();
        assertEquals(dealer.next(), minibridgeGame.getLeader());
    }

}
