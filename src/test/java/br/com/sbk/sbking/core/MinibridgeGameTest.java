package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MinibridgeGameTest {

    @Test
    public void getLeaderShouldReturnTheNextDirectionFromDealer() {
        MinibridgeGame minibridgeGame = new MinibridgeGame();
        Direction dealer = minibridgeGame.getDealer();
        assertEquals(dealer.next(), minibridgeGame.getLeader());
    }

}
