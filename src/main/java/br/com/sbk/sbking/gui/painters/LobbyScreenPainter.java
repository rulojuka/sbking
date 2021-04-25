package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.elements.AllTableCardsElement;

public class LobbyScreenPainter implements Painter {

  private List<LobbyScreenTableDTO> tables;
  private ActionListener actionListener;

  public LobbyScreenPainter(List<LobbyScreenTableDTO> tables, ActionListener actionListener) {
    this.tables = tables;
    this.actionListener = actionListener;
  }

  @Override
  public void paint(Container contentPane) {
    new AllTableCardsElement(contentPane, this.tables, this.actionListener);

    contentPane.validate();
    contentPane.repaint();
  }

}
