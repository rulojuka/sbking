package br.com.sbk.sbking.gui.jelements;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;

@SuppressWarnings("serial")
public class SitOrLeaveButton extends SBKingButton {

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 30;
  private static double scaleFactor;
  private static int width;
  private static int height;

  public SitOrLeaveButton(Direction direction) {
    super();
    this.putClientProperty("type", "SITORLEAVE");
    this.putClientProperty("direction", direction);
    width = (int) (DEFAULT_WIDTH * scaleFactor);
    height = (int) (DEFAULT_HEIGHT * scaleFactor);
    scaleFactor = FrameConstants.getScreenScale();
    this.setSize(width, height);
  }

}
