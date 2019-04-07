package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.gui.models.GameScoreboard;

public class GameScoreboardElement {
	public GameScoreboardElement(GameScoreboard gameScoreboard, Container container, Point point) {

		int x_size = 400;
		int y_size = 400;

		StringBuilder text = new StringBuilder();
		text.append("<html>");
		text.append("<span>        Current Scoreboard       </span><br/>");
		for (int i = 1; i <= 10; i++) {
			text.append("<span>");
			text.append(gameScoreboard.getLine(i));
			text.append("</span>");
			text.append("<br/>");
		}
		text.append("</html>");
		String string = text.toString();
		// System.out.println("Full html is: **"+string+"**");
		String stringWithoutSpaces = string.replaceAll(" ", "&nbsp;");
		// System.out.println("Full html is: **"+stringWithoutSpaces+"**");

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);

		JLabel label = new JLabel(stringWithoutSpaces);
		label.setFont(font);
		label.setBounds(point.x - x_size / 2, point.y - y_size / 2, x_size, y_size);
		container.add(label);
	}
}
