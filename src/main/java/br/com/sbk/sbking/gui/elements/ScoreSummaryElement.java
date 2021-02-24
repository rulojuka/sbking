package br.com.sbk.sbking.gui.elements;

import java.awt.Container;

import javax.swing.JButton;

import br.com.sbk.sbking.core.Deal;

public class ScoreSummaryElement {

	public ScoreSummaryElement(Deal board, Container container) {
		int scoreMultiplier = board.getRuleset().getScoreMultiplier();
		int size = 200;

		JButton scoreboardButton = new JButton();
		scoreboardButton.setText(
				"NS:" + board.getNorthSouthPoints() * scoreMultiplier + " EW:" + board.getEastWestPoints() * scoreMultiplier);
		container.add(scoreboardButton);
		scoreboardButton.setBounds(container.getWidth() / 2 - size / 2, container.getHeight() / 2 - size / 2, size, size);
	}

}
