package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingGameModeOrStrainPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingPositiveOrNegativePainter;

@SuppressWarnings("serial")
public class KingNetworkClientScreen extends NetworkClientScreen {

    public KingNetworkClientScreen() {
        super();
    }

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
            sleepFor(300);
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
                sleepFor(1000);
                if (sbKingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a player.");
                    }
                    LOGGER.info("Starting to paint Deal");
                    Deal currentDeal = sbKingClient.getDeal();
                    Board currentBoard = sbKingClient.getCurrentBoard();
                    if (currentDeal == null) {
                        paintDeal(currentBoard, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
                    } else {
                        paintDeal(currentDeal, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
                    }
                    LOGGER.info("Finished painting Deal");
                } else {
                    if (!sbKingClient.isPositiveOrNegativeSelected()) {
                        LOGGER.info("Positive or Negative not selected yet!");
                        if (sbKingClient.getDirection() == null || !sbKingClient.isPositiveOrNegativeChooserSet()) {
                            LOGGER.info("Direction not set yet.");
                            LOGGER.info("or Chooser not set yet.");
                            continue;
                        } else {
                            LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            LOGGER.info("My direction: " + sbKingClient.getDirection());
                            LOGGER.info("Chooser: " + sbKingClient.getGameModeOrStrainChooser());
                            paintWaitingForChoosingPositiveOrNegativeScreen(sbKingClient.getDirection(),
                                    sbKingClient.getPositiveOrNegativeChooser());
                            while (!sbKingClient.isPositiveOrNegativeSelected()) {
                                sleepFor(100);
                            }
                        }
                    } else if (!sbKingClient.isRulesetValidSet()) {
                        LOGGER.info("GameModeOrStrain not selected yet!");
                        if (sbKingClient.getDirection() == null || !sbKingClient.isGameModeOrStrainChooserSet()) {
                            LOGGER.info("Direction not set yet.");
                            LOGGER.info("or Chooser not set yet.");
                            continue;
                        } else {
                            LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            LOGGER.info("My direction: " + sbKingClient.getDirection());
                            LOGGER.info("Chooser: " + sbKingClient.getGameModeOrStrainChooser());

                            paintWaitingForChoosingGameModeOrStrainScreen(sbKingClient.getDirection(),
                                    sbKingClient.getGameModeOrStrainChooser(), sbKingClient.isPositive());
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

    private void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            LOGGER.debug(e);
        }
    }

    private void paintConnectToServerScreen() {
        Painter connectToServerPainter = new ConnectToServerPainter(this);
        this.paintPainter(connectToServerPainter);
    }

    private void paintWaitingForChoosingGameModeOrStrainScreen(Direction direction, Direction chooser,
            boolean isPositive) {
        Painter waitingForChoosingGameModeOrStrainPainter = new WaitingForChoosingGameModeOrStrainPainter(direction,
                chooser, isPositive, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
        this.paintPainter(waitingForChoosingGameModeOrStrainPainter);
    }

    private void paintWaitingForChoosingPositiveOrNegativeScreen(Direction direction, Direction chooser) {
        Painter waitingForChoosingPositiveOrNegativePainter = new WaitingForChoosingPositiveOrNegativePainter(direction,
                chooser, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
        this.paintPainter(waitingForChoosingPositiveOrNegativePainter);
    }

    private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
        this.paintPainter(dealPainter);
    }

    private void paintDeal(Board board, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, board);
        this.paintPainter(dealPainter);
    }

}
