package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.WaitingForPlayersLabelElement;
import br.com.sbk.sbking.gui.elements.YouArePlayerElement;

public class WaitingForPlayersPainter implements Painter {

    private Direction direction;

    public WaitingForPlayersPainter(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void paint(Container contentPane) {
        YouArePlayerElement.add(this.direction, contentPane);
        WaitingForPlayersLabelElement.add(contentPane);
        contentPane.validate();
        contentPane.repaint();
    }

}
