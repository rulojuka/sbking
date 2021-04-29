package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class YourDirectionIsMessage implements SBKingMessage {

  private Direction direction;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private YourDirectionIsMessage() {
  }

  public YourDirectionIsMessage(Direction direction) {
    this.direction = direction;
  }

  @Override
  public Direction getContent() {
    return this.direction;
  }

}
