package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class CurrentPlayerElement {

    private static final int X_OFFSET = 0;
    private static final int Y_OFFSET = -270;
    private static final java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255, 0, 0);

    public CurrentPlayerElement(Direction currentPlayer, Container container, boolean isMyTurn) {
        int width = 110;
        int height = 15;

        Point tableCenter = new Point(FrameConstants.HALF_WIDTH, FrameConstants.HALF_HEIGHT);
        Point currentPlayerPosition = new Point(tableCenter);
        currentPlayerPosition.translate(X_OFFSET - width / 2, 20 + (int) (Y_OFFSET * FrameConstants.getScreenScale()));

        String text;

        if (isMyTurn) {
            text = "It is your turn.";
        } else {
            text = currentPlayer.getCompleteName() + " to play.";
        }

        JLabel playerButton = new SBKingLabel(text);
        playerButton.setHorizontalAlignment(CENTER);
        playerButton.setSize(width, height);
        playerButton.setLocation(currentPlayerPosition);
        playerButton.setBackground(TURN_LIGHT_COLOR);
        playerButton.setOpaque(true);
        container.add(playerButton);
    }

}
