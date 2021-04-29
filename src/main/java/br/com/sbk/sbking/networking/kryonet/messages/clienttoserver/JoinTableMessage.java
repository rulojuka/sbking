package br.com.sbk.sbking.networking.kryonet.messages.clienttoserver;

import java.util.UUID;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class JoinTableMessage implements SBKingMessage {

  private UUID tableId;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private JoinTableMessage() {
  }

  public JoinTableMessage(UUID tableId) {
    this.tableId = tableId;
  }

  @Override
  public UUID getContent() {
    return this.tableId;
  }

}
