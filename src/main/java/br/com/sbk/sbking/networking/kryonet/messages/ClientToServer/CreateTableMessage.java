package br.com.sbk.sbking.networking.kryonet.messages.ClientToServer;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class CreateTableMessage implements SBKingMessage {

  private String gameName;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private CreateTableMessage() {
  }

  public CreateTableMessage(String gameName) {
    this.gameName = gameName;
  }

  @Override
  public String getContent() {
    return this.gameName;
  }

}
