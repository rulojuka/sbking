package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Deque;

import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class CagandoNoBequinhoGameTest {

  @Test
  public void getLeaderShouldReturnTheDealer() {
    @SuppressWarnings("unchecked")
    Deque<Card> deck = mock(Deque.class);
    CagandoNoBequinhoGame cagandoNoBequinhoGame = new CagandoNoBequinhoGame(deck);
    Direction dealer = cagandoNoBequinhoGame.getDealer();
    assertEquals(dealer, cagandoNoBequinhoGame.getLeader());
  }

}
