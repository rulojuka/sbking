package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Deque;

import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class PositiveKingGameTest {

  @Test
  public void getLeaderShouldReturnTheOppositeDirectionFromDealer() {
    Deque<Card> deck = mock(Deque.class);
    PositiveKingGame positiveKingGame = new PositiveKingGame(deck);
    Direction dealer = positiveKingGame.getDealer();
    assertEquals(dealer.next(2), positiveKingGame.getLeader());
  }

}
