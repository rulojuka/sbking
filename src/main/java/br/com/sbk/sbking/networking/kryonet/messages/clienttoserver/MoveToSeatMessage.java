package br.com.sbk.sbking.networking.kryonet.messages.clienttoserver;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class MoveToSeatMessage implements SBKingMessage {

  private Direction direction;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  @Deprecated
  private MoveToSeatMessage() {
  }

  public MoveToSeatMessage(Direction direction) {
    this.direction = direction;
  }

  @Override
  public Direction getContent() {
    return this.direction;
  }

}
