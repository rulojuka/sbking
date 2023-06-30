package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import javax.swing.JLabel;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public final class WaitingForPlayersLabelElement {

    private WaitingForPlayersLabelElement() {
        throw new IllegalStateException("Utility class");
    }

    public static void add(Container container) {

        JLabel rulesetLabel = new SBKingLabel("Waiting for players...");
        rulesetLabel.setHorizontalAlignment(CENTER);
        int width = 300;
        int height = 15;
        rulesetLabel.setSize(width, height);
        rulesetLabel.setLocation(FrameConstants.getTableWidth() / 2 - width / 2,
                FrameConstants.getTableHeight() / 2 - height / 2);
        container.add(rulesetLabel);
    }

}
