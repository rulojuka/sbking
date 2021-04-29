package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.gui.jelements.SBKingLabel;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class GameScoreboardElement {
    public GameScoreboardElement(KingGameScoreboard gameScoreboard, Container container, Point gameScoreboardCenter) {
        int width = 300;
        int height = 250;

        int xOffset = width / 2;
        xOffset *= -1;
        int yOffset = height / 2;
        yOffset *= -1;

        Point gameScoreboardTopLeftCorner = new Point(gameScoreboardCenter);
        gameScoreboardTopLeftCorner.translate(xOffset, yOffset);

        StringBuilder text = new StringBuilder();
        text.append("<html>");
        text.append("<span>        Current Scoreboard       </span><br/>");
        for (int i = 1; i <= 10; i++) {
            text.append("<span>");
            text.append(gameScoreboard.getLine(i));
            text.append("</span>");
            text.append("<br/>");
        }
        text.append("<span>");
        text.append(gameScoreboard.getSummary());
        text.append("</span>");
        text.append("<br/>");
        text.append("</html>");
        String string = text.toString();
        String stringWithoutSpaces = string.replaceAll(" ", "&nbsp;");

        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);

        JLabel label = new SBKingLabel(stringWithoutSpaces);
        label.setFont(font);
        label.setLocation(gameScoreboardTopLeftCorner);
        label.setSize(width, height);
        container.add(label);
    }
}
