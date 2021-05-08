package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.sbk.sbking.core.Direction;

public class KingGameTest {

  @Test
  public void getLeaderShouldReturnTheOppositeDirectionFromDealer() {
    KingGame kingGame = new KingGame();
    Direction dealer = kingGame.getDealer();
    assertEquals(dealer.next(2), kingGame.getLeader());
  }

}
