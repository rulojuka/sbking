package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import java.util.List;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class GetTableSpectatorsResponseMessage implements SBKingMessage {

  private List<String> spectators;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private GetTableSpectatorsResponseMessage() {
  }

  public GetTableSpectatorsResponseMessage(List<String> spectators) {
    this.spectators = spectators;
  }

  @Override
  public Object getContent() {
    return this.spectators;
  }

}
