package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class TextMessage implements SBKingMessage {

  private String text;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private TextMessage() {
  }

  public TextMessage(String text) {
    this.text = text;
  }

  @Override
  public Object getContent() {
    return this.text;
  }

}
