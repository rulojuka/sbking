package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;

@SuppressWarnings("serial")
public class CagandoNetworkClientScreen extends NetworkClientScreen {

    public CagandoNetworkClientScreen() {
        super();
    }

    @Override
    public void run() {
        LOGGER.info("Starting to paint ConnectToServerScreen");
        paintConnectToServerScreen();
        LOGGER.info("Finished painting ConnectToServerScreen");

        LOGGER.info("Waiting for connectedToServer to be true");
        while (!connectedToServer) {
            sleepFor(100);
        }

        LOGGER.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbKingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            if (sbKingClient.isSpectator()) {
                if (sbKingClient.getBoardHasChanged() || sbKingClient.getDealHasChanged()
                        || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a spectator.");
                    }
                    Deal currentDeal = sbKingClient.getDeal();
                    Board currentBoard = sbKingClient.getCurrentBoard();
                    if (currentDeal == null) {
                        paintSpectatorScreen(currentBoard, sbKingClient.getPlayCardActionListener());
                    } else {
                        paintSpectatorScreen(currentDeal, sbKingClient.getPlayCardActionListener());
                    }
                }
            } else {
                if (sbKingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a player.");
                    }
                    Deal currentDeal = sbKingClient.getDeal();

                    LOGGER.info("Starting to paint Deal");
                    paintDeal(currentDeal, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
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
        this.paintPainter(dealPainter);
    }

    private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
        if (deal == null) {
            LOGGER.error("Deal should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
            this.paintPainter(spectatorPainter);
        }
    }

    private void paintSpectatorScreen(Board board, ActionListener playCardActionListener) {
        if (board == null) {
            LOGGER.error("Board should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, board);
            this.paintPainter(spectatorPainter);
        }
    }

}
