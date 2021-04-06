package br.com.sbk.sbking.networking.kryonet.messages.ServerToClient;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class BoardMessage implements SBKingMessage {

  private Board board;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private BoardMessage() {
  }

  public BoardMessage(Board board) {
    this.board = board;
  }

  @Override
  public Board getContent() {
    return this.board;
  }

}
