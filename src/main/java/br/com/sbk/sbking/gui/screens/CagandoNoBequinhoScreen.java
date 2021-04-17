package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

@SuppressWarnings("serial")
public class CagandoNoBequinhoScreen {

    private SBKingClientJFrame sbkingClientJFrame;
    private SBKingClient sbkingClient;

    public CagandoNoBequinhoScreen(SBKingClientJFrame sbkingClientJFrame, SBKingClient sbkingClient) {
        this.sbkingClientJFrame = sbkingClientJFrame;
        this.sbkingClient = sbkingClient;
    }

    public void run() {
        LOGGER.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbkingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            if (sbkingClient.isSpectator()) {
                if (sbkingClient.getBoardHasChanged() || sbkingClient.getDealHasChanged()
                        || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a spectator.");
                    }
                    Deal currentDeal = sbkingClient.getDeal();
                    Board currentBoard = sbkingClient.getCurrentBoard();
                    if (currentDeal == null) {
                        paintSpectatorScreen(currentBoard, sbkingClient.getPlayCardActionListener());
                    } else {
                        paintSpectatorScreen(currentDeal, sbkingClient.getPlayCardActionListener());
                    }
                }
            } else {
                if (sbkingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a player.");
                    }
                    Deal currentDeal = sbkingClient.getDeal();

                    LOGGER.info("Starting to paint Deal");
                    paintDeal(currentDeal, sbkingClient.getDirection(), sbkingClient.getPlayCardActionListener());
                    LOGGER.info("Finished painting Deal");
                }

            }
            sleepFor(300);
        }
    }

    private void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            LOGGER.debug(e);
        }
    }

    private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
        sbkingClientJFrame.paintPainter(dealPainter);
    }

    private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
        if (deal == null) {
            LOGGER.error("Deal should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
            sbkingClientJFrame.paintPainter(spectatorPainter);
        }
    }

    private void paintSpectatorScreen(Board board, ActionListener playCardActionListener) {
        if (board == null) {
            LOGGER.error("Board should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, board);
            sbkingClientJFrame.paintPainter(spectatorPainter);
        }
    }

}
