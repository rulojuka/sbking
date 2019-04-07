package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import static br.com.sbk.sbking.gui.constants.FrameConstants.*;

public class WaitingForChoosingPainter {

	public WaitingForChoosingPainter() {
		// TODO Auto-generated constructor stub
	}

	public void paint(Container contentPane) {
		Point middleOfScreen = new Point(TABLE_WIDTH/2,TABLE_HEIGHT/2);
		new GameScoreboardElement(new GameScoreboard(), contentPane, middleOfScreen);
		contentPane.validate();
		contentPane.repaint();
	}

}
