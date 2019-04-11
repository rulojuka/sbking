package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.models.GameScoreboard;

public class FinalScoreboardPainter {

	private GameScoreboard gameScoreboard;

	public FinalScoreboardPainter(GameScoreboard gameScoreboard) {
		this.gameScoreboard = gameScoreboard;
	}

	public void paint(Container contentPane) {
		Point middleOfScreen = new Point(TABLE_WIDTH / 2, TABLE_HEIGHT / 2);
		new GameScoreboardElement(gameScoreboard, contentPane, middleOfScreen);
		contentPane.validate();
		contentPane.repaint();
	}

}
