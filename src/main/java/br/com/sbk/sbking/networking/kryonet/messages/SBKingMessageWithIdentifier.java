package br.com.sbk.sbking.networking.kryonet.messages;

import java.util.UUID;

public class SBKingMessageWithIdentifier {

  private SBKingMessage message;
  private UUID identifier;

  public SBKingMessageWithIdentifier(SBKingMessage message, UUID identifier) {
    this.message = message;
    this.identifier = identifier;
  }

  public SBKingMessage getMessage() {
    return message;
  }

  public UUID getIdentifier() {
    return identifier;
  }

}
