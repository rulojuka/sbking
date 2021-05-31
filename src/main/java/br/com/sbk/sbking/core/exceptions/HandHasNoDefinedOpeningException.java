package br.com.sbk.sbking.core.exceptions;

import br.com.sbk.sbking.core.Hand;

public class HandHasNoDefinedOpeningException extends RuntimeException {

  private Hand hand;

  public HandHasNoDefinedOpeningException(Hand hand) {
    super("This hand has no defined opening.");
    this.hand = hand;
  }

  public Hand getHand() {
    return hand;
  }

}
