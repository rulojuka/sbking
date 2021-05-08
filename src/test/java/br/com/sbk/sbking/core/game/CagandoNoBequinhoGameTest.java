package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.sbk.sbking.core.Direction;

public class CagandoNoBequinhoGameTest {

  @Test
  public void getLeaderShouldReturnTheDealer() {
    CagandoNoBequinhoGame cagandoNoBequinhoGame = new CagandoNoBequinhoGame();
    Direction dealer = cagandoNoBequinhoGame.getDealer();
    assertEquals(dealer, cagandoNoBequinhoGame.getLeader());
  }

}
