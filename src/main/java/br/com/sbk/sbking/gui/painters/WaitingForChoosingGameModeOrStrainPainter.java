package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChooseGameModeOrStrainElement;
import br.com.sbk.sbking.gui.elements.EssentialDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.WaitingForChooserElement;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WaitingForChoosingGameModeOrStrainPainter implements Painter {

    private Direction myDirection;
    private Direction chooserDirection;
    private boolean isPositive;
    private SBKingClient sbKingClient;

    public WaitingForChoosingGameModeOrStrainPainter(Direction myDirection, Direction chooserDirection,
            boolean isPositive, SBKingClient sbKingClient) {
        this.myDirection = myDirection;
        this.chooserDirection = chooserDirection;
        this.isPositive = isPositive;
        this.sbKingClient = sbKingClient;
    }

    @Override
    public void paint(Container contentPane) {
        if (myDirection != chooserDirection) {
            WaitingForChooserElement.add(contentPane, chooserDirection, "Game Mode or Strain.");
        } else {
            ChooseGameModeOrStrainElement chooseGameModeOrStrainElement = new ChooseGameModeOrStrainElement(contentPane,
                    this.sbKingClient, this.isPositive);
            chooseGameModeOrStrainElement.add();
        }

        new EssentialDirectionBoardElements(this.sbKingClient.getDeal(), contentPane,
                this.sbKingClient.getActionListener(), this.sbKingClient.getSpectatorNames(), myDirection,
                this.sbKingClient.getGameName());

        contentPane.validate();
        contentPane.repaint();

    }

}
