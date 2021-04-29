package br.com.sbk.sbking.networking.kryonet.messages.clienttoserver;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class PlayCardMessage implements SBKingMessage {

  private Card card;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private PlayCardMessage() {
  }

  public PlayCardMessage(Card card) {
    this.card = card;
  }

  @Override
  public Card getContent() {
    return this.card;
  }

}
