package br.com.sbk.sbking.gui.jelements;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

@SuppressWarnings("serial")
public class SitOrLeaveButton extends SBKingButton {

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 30;

  public SitOrLeaveButton(Direction direction) {
    super();
    this.putClientProperty("type", "SITORLEAVE");
    this.putClientProperty("direction", direction);
    double scaleFactor = FrameConstants.getScreenScale();
    int width = (int) (DEFAULT_WIDTH * scaleFactor);
    int height = (int) (DEFAULT_HEIGHT * scaleFactor);
    this.setSize(width, height);
  }

}
