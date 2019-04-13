package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChooseGameModeOrStrainElement;
import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.elements.HandWhileChoosingElement;
import br.com.sbk.sbking.gui.elements.WaitingForChooserGameModeOrStrainElement;
import br.com.sbk.sbking.gui.elements.YouArePlayerElement;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WaitingForChoosingGameModeOrStrainPainter {

	private Direction myDirection;
	private Direction chooserDirection;
	private boolean isPositive;
	private SBKingClient sbKingClient;
	private GameScoreboard gameScoreboard;

	public WaitingForChoosingGameModeOrStrainPainter(Direction myDirection, Direction chooserDirection,
			boolean isPositive, SBKingClient sbKingClient, GameScoreboard gameScoreboard) {
		this.myDirection = myDirection;
		this.chooserDirection = chooserDirection;
		this.isPositive = isPositive;
		this.sbKingClient = sbKingClient;
		this.gameScoreboard = gameScoreboard;
	}

	public void paint(Container contentPane) {
		int xCenterLocation = TABLE_WIDTH - 160;
		int yCenterLocation = 110;
		Point centerOfScoreboardPosition = new Point(xCenterLocation, yCenterLocation);
		new GameScoreboardElement(gameScoreboard, contentPane, centerOfScoreboardPosition);
		if (myDirection != chooserDirection) {
			YouArePlayerElement.add(this.myDirection, contentPane);
			WaitingForChooserGameModeOrStrainElement.add(contentPane, chooserDirection);
		} else {
			ChooseGameModeOrStrainElement chooseGameModeOrStrainElement = new ChooseGameModeOrStrainElement(contentPane,
					this.chooserDirection, this.sbKingClient, this.isPositive);
			chooseGameModeOrStrainElement.add();
		}

		new HandWhileChoosingElement(contentPane, this.sbKingClient.getCurrentBoard(), this.myDirection);

		contentPane.validate();
		contentPane.repaint();

	}

}
