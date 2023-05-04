package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class DealMessage implements SBKingMessage {

  private Deal deal;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  @Deprecated
  private DealMessage() {
  }

  public DealMessage(Deal deal) {
    this.deal = deal;
  }

  @Override
  public Object getContent() {
    return this.deal;
  }
}
