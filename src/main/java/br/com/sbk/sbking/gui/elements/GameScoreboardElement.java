package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.gui.JElements.SBKingLabel;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class GameScoreboardElement {
	public GameScoreboardElement(KingGameScoreboard gameScoreboard, Container container, Point gameScoreboardCenter) {
		int width = 300;
		int height = 250;

		int x_offset = width / 2;
		x_offset *= -1;
		int y_offset = height / 2;
		y_offset *= -1;

		Point gameScoreboardTopLeftCorner = new Point(gameScoreboardCenter);
		gameScoreboardTopLeftCorner.translate(x_offset, y_offset);

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
		// System.out.println("Full html is: **"+string+"**");
		String stringWithoutSpaces = string.replaceAll(" ", "&nbsp;");
		// System.out.println("Full html is: **"+stringWithoutSpaces+"**");

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);

		JLabel label = new SBKingLabel(stringWithoutSpaces);
		label.setFont(font);
		label.setLocation(gameScoreboardTopLeftCorner);
		label.setSize(width, height);
		container.add(label);
	}
}
