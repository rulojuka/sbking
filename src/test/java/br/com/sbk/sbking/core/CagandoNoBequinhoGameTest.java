package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CagandoNoBequinhoGameTest {

  @Test
  public void getLeaderShouldReturnTheDealer() {
    CagandoNoBequinhoGame cagandoNoBequinhoGame = new CagandoNoBequinhoGame();
    Direction dealer = cagandoNoBequinhoGame.getDealer();
    assertEquals(dealer, cagandoNoBequinhoGame.getLeader());
  }

}
