package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public final class WaitingForChooserElement {

    private WaitingForChooserElement() {
        throw new IllegalStateException("Utility class");
    }

    public static void add(Container container, Direction direction, String text) {
        String labelTextPrefix = "<html>Waiting for " + direction.toString() + " to choose<br/>";
        String labelTextSuffix = "</html>";
        String completeLabelText = labelTextPrefix + text + labelTextSuffix;

        JLabel waitingLabel = new SBKingLabel(completeLabelText);
        waitingLabel.setHorizontalAlignment(CENTER);
        int width = 300;
        int height = 30;
        int x = FrameConstants.getHalfWidth();
        int y = FrameConstants.getHalfHeight();
        waitingLabel.setSize(width, height);
        waitingLabel.setLocation(x - width / 2, y - height / 2);
        container.add(waitingLabel);
    }

}
