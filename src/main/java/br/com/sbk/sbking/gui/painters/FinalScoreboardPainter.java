package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class FinalScoreboardPainter implements Painter {

    private KingGameScoreboard gameScoreboard;

    public FinalScoreboardPainter(KingGameScoreboard gameScoreboard) {
        this.gameScoreboard = gameScoreboard;
    }

    @Override
    public void paint(Container contentPane) {
        int xCenterLocation = HALF_WIDTH;
        int yCenterLocation = HALF_HEIGHT;
        Point centerOfScoreboardPosition = new Point(xCenterLocation, yCenterLocation);

        new GameScoreboardElement(gameScoreboard, contentPane, centerOfScoreboardPosition);
        contentPane.validate();
        contentPane.repaint();
    }

}
