package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.gui.constants.FontConstants;
import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public class GameNameElement {

    private static final int DEFAULT_FONT_SIZE = 40;
    private static final int DEFAULT_WIDTH = 500;
    private static double scaleFactor;

    public GameNameElement(String gameName, Container container, Point position) {
        scaleFactor = FrameConstants.getScreenScale();
        int fontSize = (int) (scaleFactor * DEFAULT_FONT_SIZE);
        int labelWidth = (int) (DEFAULT_WIDTH * scaleFactor);
        int labelHeight = (int) (fontSize * 1.1);
        position.translate(-labelWidth / 2, 0);

        JLabel gameNameLabel = new SBKingLabel(gameName);
        gameNameLabel.setSize(labelWidth, labelHeight);
        gameNameLabel.setHorizontalAlignment(CENTER);
        gameNameLabel.setLocation(position);
        gameNameLabel.setFont(new Font(FontConstants.FONT_NAME, Font.BOLD, fontSize));
        container.add(gameNameLabel);
    }

}
