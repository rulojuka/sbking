package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.tableWidth;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChoosePositiveOrNegativeElement;
import br.com.sbk.sbking.gui.elements.EssentialDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.elements.WaitingForChooserElement;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WaitingForChoosingPositiveOrNegativePainter implements Painter {

    private Direction myDirection;
    private Direction chooserDirection;
    private SBKingClient sbKingClient;
    private KingGameScoreboard gameScoreboard;

    public WaitingForChoosingPositiveOrNegativePainter(Direction myDirection, Direction chooserDirection,
            SBKingClient sbKingClient, KingGameScoreboard gameScoreboard) {
        this.myDirection = myDirection;
        this.chooserDirection = chooserDirection;
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
            WaitingForChooserElement.add(contentPane, chooserDirection, "Positive or Negative.");
        } else {
            ChoosePositiveOrNegativeElement choosePositiveOrNegativeElement = new ChoosePositiveOrNegativeElement(
                    contentPane, this.sbKingClient);
            choosePositiveOrNegativeElement.add();
        }

        new EssentialDirectionBoardElements(this.sbKingClient.getDeal(), contentPane,
                this.sbKingClient.getActionListener(), this.sbKingClient.getSpectatorNames(), myDirection);

        contentPane.validate();
        contentPane.repaint();
    }

}
