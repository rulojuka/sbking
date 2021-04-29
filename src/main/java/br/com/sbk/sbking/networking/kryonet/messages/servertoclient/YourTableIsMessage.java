package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class YourTableIsMessage implements SBKingMessage {

  private String gameName;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private YourTableIsMessage() {
  }

  public YourTableIsMessage(String gameName) {
    this.gameName = gameName;
  }

  @Override
  public String getContent() {
    return this.gameName;
  }

}
