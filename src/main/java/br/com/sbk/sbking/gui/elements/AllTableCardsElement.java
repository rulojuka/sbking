package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.tableWidth;
import static br.com.sbk.sbking.gui.constants.FrameConstants.tableHeight;

import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Point;
import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;

public class AllTableCardsElement {

  public AllTableCardsElement(Container container, List<LobbyScreenTableDTO> tables, ActionListener actionListener) {

    Point position = new Point(tableWidth / 4, tableHeight / 5);
    for (LobbyScreenTableDTO table : tables) {
      new TableCardElement(container, table, (Point) position.clone(), actionListener);
      position.translate(200, 0);
    }
  }

}
