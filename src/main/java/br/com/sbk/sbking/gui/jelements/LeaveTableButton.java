package br.com.sbk.sbking.gui.jelements;

public class LeaveTableButton extends SBKingButton {

  public LeaveTableButton() {
    super();
    this.putClientProperty("type", "LEAVETABLE");
    this.setText("Leave Table");
    this.setSize(140, 20);
  }
}
