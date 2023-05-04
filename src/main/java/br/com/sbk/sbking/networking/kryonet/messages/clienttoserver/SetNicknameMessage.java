package br.com.sbk.sbking.networking.kryonet.messages.clienttoserver;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class SetNicknameMessage implements SBKingMessage {

  private String nickname;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  @Deprecated
  private SetNicknameMessage() {
  }

  public SetNicknameMessage(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public String getContent() {
    return this.nickname;
  }

}
