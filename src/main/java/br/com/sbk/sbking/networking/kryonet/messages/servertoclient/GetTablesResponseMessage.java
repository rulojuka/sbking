package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class GetTablesResponseMessage implements SBKingMessage {

  private List<LobbyScreenTableDTO> tables;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  @Deprecated
  private GetTablesResponseMessage() {
  }

  public GetTablesResponseMessage(List<LobbyScreenTableDTO> tables) {
    this.tables = tables;
  }

  @Override
  public Object getContent() {
    return this.tables;
  }
}
