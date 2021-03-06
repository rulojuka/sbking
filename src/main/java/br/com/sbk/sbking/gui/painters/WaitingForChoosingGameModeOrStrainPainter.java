package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.tableWidth;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChooseGameModeOrStrainElement;
import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.WaitingForChooserElement;
import br.com.sbk.sbking.gui.elements.YouArePlayerElement;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WaitingForChoosingGameModeOrStrainPainter implements Painter {

    private Direction myDirection;
    private Direction chooserDirection;
    private boolean isPositive;
    private SBKingClient sbKingClient;
    private KingGameScoreboard gameScoreboard;

    public WaitingForChoosingGameModeOrStrainPainter(Direction myDirection, Direction chooserDirection,
            boolean isPositive, SBKingClient sbKingClient, KingGameScoreboard gameScoreboard) {
        this.myDirection = myDirection;
        this.chooserDirection = chooserDirection;
        this.isPositive = isPositive;
        this.sbKingClient = sbKingClient;
        this.gameScoreboard = gameScoreboard;
    }

    @Override
    public void paint(Container contentPane) {
        int xCenterLocation = tableWidth - 160;
        int yCenterLocation = 110;
        Point centerOfScoreboardPosition = new Point(xCenterLocation, yCenterLocation);
        new GameScoreboardElement(gameScoreboard, contentPane, centerOfScoreboardPosition);
        if (myDirection != chooserDirection) {
            YouArePlayerElement.add(this.myDirection, contentPane);
            WaitingForChooserElement.add(contentPane, chooserDirection, "Game Mode or Strain.");
        } else {
            ChooseGameModeOrStrainElement chooseGameModeOrStrainElement = new ChooseGameModeOrStrainElement(contentPane,
                    this.sbKingClient, this.isPositive);
            chooseGameModeOrStrainElement.add();
        }

        new SpecificDirectionBoardElements(this.myDirection, this.sbKingClient.getDeal(), contentPane,
                this.sbKingClient.getActionListener());

        contentPane.validate();
        contentPane.repaint();

    }

}
