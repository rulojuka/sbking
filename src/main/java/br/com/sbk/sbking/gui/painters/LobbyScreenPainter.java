package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.gui.elements.CreateTableElement;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class LobbyScreenPainter implements Painter {

  private SBKingClient sbKingClient;

  public LobbyScreenPainter(SBKingClient sbKingClient) {
    this.sbKingClient = sbKingClient;
  }

  @Override
  public void paint(Container contentPane) {
    new CreateTableElement(contentPane, this.sbKingClient);

    contentPane.validate();
    contentPane.repaint();
  }

}
