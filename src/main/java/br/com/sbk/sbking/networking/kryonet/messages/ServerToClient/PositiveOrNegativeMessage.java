package br.com.sbk.sbking.networking.kryonet.messages.ServerToClient;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class PositiveOrNegativeMessage implements SBKingMessage {

  private String positiveOrNegative;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private PositiveOrNegativeMessage() {
  }

  public PositiveOrNegativeMessage(String positiveOrNegative) {
    this.positiveOrNegative = positiveOrNegative;
  }

  @Override
  public String getContent() {
    return this.positiveOrNegative;
  }

}
