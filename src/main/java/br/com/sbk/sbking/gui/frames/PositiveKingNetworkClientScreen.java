package br.com.sbk.sbking.gui.frames;

import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingGameModeOrStrainPainter;

@SuppressWarnings("serial")
public class PositiveKingNetworkClientScreen extends NetworkClientScreen {

    final static Logger logger = LogManager.getLogger(PositiveKingNetworkClientScreen.class);

    public PositiveKingNetworkClientScreen() {
        super();
    }

    public void run() {
        logger.info("Starting to paint ConnectToServerScreen");
        paintConnectToServerScreen();
        logger.info("Finished painting ConnectToServerScreen");

        logger.info("Waiting for connectedToServer to be true");
        while (!connectedToServer) {
            sleepFor(100);
        }

        logger.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbKingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            sleepFor(300);
            if (sbKingClient.isSpectator()) {
                if (sbKingClient.getBoardHasChanged() || sbKingClient.getDealHasChanged()
                        || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        logger.info("Deal has changed. Painting deal.");
                        logger.info("It is a spectator.");
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
                sleepFor(1000);
                if (sbKingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        logger.info("Deal has changed. Painting deal.");
                        logger.info("It is a player.");
                    }
                    logger.info("Starting to paint Deal");
                    paintDeal(sbKingClient.getDeal(), sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
                    logger.info("Finished painting Deal");
                } else {
                    if (!sbKingClient.isRulesetValidSet()) {
                        logger.info("Suit not selected yet!");
                        if (sbKingClient.getDirection() == null || !sbKingClient.isGameModeOrStrainChooserSet()) {
                            logger.info("Direction not set yet.");
                            logger.info("or Chooser not set yet.");
                            continue;
                        } else {
                            logger.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            logger.info("My direction: " + sbKingClient.getDirection());
                            logger.info("Chooser: " + sbKingClient.getGameModeOrStrainChooser());

                            paintWaitingForChoosingGameModeOrStrainScreen(sbKingClient.getDirection(),
                                    sbKingClient.getGameModeOrStrainChooser(), true);
                            while (!sbKingClient.isRulesetValidSet()) {
                                sleepFor(100);
                            }
                        }
                    }
                }
            }
        }
    }

    private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
        if (deal == null) {
            logger.error("Deal should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
            this.paintPainter(spectatorPainter);
        }
    }

    private void paintSpectatorScreen(Board board, ActionListener playCardActionListener) {
        if (board == null) {
            logger.error("Board should not be null here.");
        } else {
            Painter spectatorPainter = new SpectatorPainter(playCardActionListener, board);
            this.paintPainter(spectatorPainter);
        }
    }

    private void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            logger.debug(e);
        }
    }

    private void paintConnectToServerScreen() {
        Painter connectToServerPainter = new ConnectToServerPainter(this);
        this.paintPainter(connectToServerPainter);
    }

    private void paintWaitingForChoosingGameModeOrStrainScreen(Direction direction, Direction chooser,
            boolean isPositive) {
        Painter waitingForChoosingGameModeOrStrainPainter = new WaitingForChoosingGameModeOrStrainPainter(direction,
                chooser, true, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
        this.paintPainter(waitingForChoosingGameModeOrStrainPainter);
    }

    private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
        this.paintPainter(dealPainter);
    }

}
