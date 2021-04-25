package br.com.sbk.sbking.gui.JElements;

import java.util.UUID;

public class JoinTableButton extends SBKingButton {

  public JoinTableButton(UUID tableId) {
    super();
    this.putClientProperty("type", "JOINTABLE");
    this.putClientProperty("tableId", tableId);
    this.setText("Spectate!");
    this.setSize(160, 20);
  }
}
