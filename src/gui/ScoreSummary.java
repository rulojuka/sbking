package gui;

import java.awt.Container;

import javax.swing.JButton;

import core.Board;

public class ScoreSummary {

	public ScoreSummary(Board board, Container container) {
		int scoreMultiplier = board.getRuleset().getScoreMultiplier();
		int size = 200;

		JButton scoreboardButton = new JButton();
		scoreboardButton.setText("NS:" + board.getNorthSouthPoints() * scoreMultiplier + " EW:"
				+ board.getEastWestPoints() * scoreMultiplier);
		container.add(scoreboardButton);
		scoreboardButton.setBounds(container.getWidth() / 2 - size/2, container.getHeight() / 2 - size/2, size, size);
	}

}
