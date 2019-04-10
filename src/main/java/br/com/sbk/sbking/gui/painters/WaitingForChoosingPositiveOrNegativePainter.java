package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChoosePositiveOrNegativeElement;
import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.elements.WaitingForChooserPositiveOrNegativeElement;
import br.com.sbk.sbking.gui.elements.YouArePlayerElement;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.networking.SBKingClient;

public class WaitingForChoosingPositiveOrNegativePainter {

	private Direction myDirection;
	private Direction chooserDirection;
	private SBKingClient sbKingClient;

	public WaitingForChoosingPositiveOrNegativePainter(Direction myDirection, Direction chooserDirection, SBKingClient sbKingClient) {
		this.myDirection = myDirection;
		this.chooserDirection = chooserDirection;
		this.sbKingClient = sbKingClient;
	}

	public void paint(Container contentPane, GameScoreboard gameScoreboard) {
		Point middleOfScreen = new Point(TABLE_WIDTH / 2, TABLE_HEIGHT / 2);
		new GameScoreboardElement(gameScoreboard, contentPane, middleOfScreen);
		if (myDirection != chooserDirection) {
			YouArePlayerElement.add(this.myDirection, contentPane);
			WaitingForChooserPositiveOrNegativeElement.add(contentPane, chooserDirection);
		} else {
			ChoosePositiveOrNegativeElement choosePositiveOrNegativeElement = new ChoosePositiveOrNegativeElement(
					contentPane, this.chooserDirection, this.sbKingClient);
			choosePositiveOrNegativeElement.add();
		}
		contentPane.validate();
		contentPane.repaint();
	}

}
