package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.gui.elements.WaitingForPlayersLabelElement;

public class WaitingForPlayersPainter implements Painter {

    @Override
    public void paint(Container contentPane) {
        WaitingForPlayersLabelElement.add(contentPane);
        contentPane.validate();
        contentPane.repaint();
    }

}
