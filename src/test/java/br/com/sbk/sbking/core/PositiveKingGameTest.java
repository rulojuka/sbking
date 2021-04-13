package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositiveKingGameTest {

  @Test
  public void getLeaderShouldReturnTheOppositeDirectionFromDealer() {
    PositiveKingGame positiveKingGame = new PositiveKingGame();
    Direction dealer = positiveKingGame.getDealer();
    assertEquals(dealer.next(2), positiveKingGame.getLeader());
  }

}
