package br.com.sbk.sbking.networking.kryonet.messages.clienttoserver;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class ChooseGameModeOrStrainMessage implements SBKingMessage {

  private String gameModeOrStrain;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private ChooseGameModeOrStrainMessage() {
  }

  public ChooseGameModeOrStrainMessage(String gameModeOrStrain) {
    this.gameModeOrStrain = gameModeOrStrain;
  }

  @Override
  public String getContent() {
    return this.gameModeOrStrain;
  }

}
