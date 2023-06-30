package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class AllTableCardsElement {

  public AllTableCardsElement(Container container, List<LobbyScreenTableDTO> tables, ActionListener actionListener) {

    Point position = new Point(FrameConstants.getTableWidth() / 4, FrameConstants.getTableHeight() / 5);
    for (LobbyScreenTableDTO table : tables) {
      new TableCardElement(container, table, (Point) position.clone(), actionListener);
      position.translate(200, 0);
    }
  }

}
