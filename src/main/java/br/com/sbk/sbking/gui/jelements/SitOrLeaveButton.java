package br.com.sbk.sbking.gui.jelements;

import br.com.sbk.sbking.core.Direction;

@SuppressWarnings("serial")
public class SitOrLeaveButton extends SBKingButton {

  public SitOrLeaveButton(Direction direction) {
    super();
    this.putClientProperty("type", "SITORLEAVE");
    this.putClientProperty("direction", direction);
    this.setSize(200, 15);
  }

}
