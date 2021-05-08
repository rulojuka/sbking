package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.sbk.sbking.core.Direction;

public class PositiveKingGameTest {

  @Test
  public void getLeaderShouldReturnTheOppositeDirectionFromDealer() {
    PositiveKingGame positiveKingGame = new PositiveKingGame();
    Direction dealer = positiveKingGame.getDealer();
    assertEquals(dealer.next(2), positiveKingGame.getLeader());
  }

}
