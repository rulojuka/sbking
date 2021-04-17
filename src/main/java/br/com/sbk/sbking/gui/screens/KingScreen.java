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
import br.com.sbk.sbking.gui.painters.WaitingForChoosingGameModeOrStrainPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingPositiveOrNegativePainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

@SuppressWarnings("serial")
public class KingScreen {

    private SBKingClientJFrame sbkingClientJFrame;
    private SBKingClient sbkingClient;

    public KingScreen(SBKingClientJFrame sbkingClientJFrame, SBKingClient sbkingClient) {
        this.sbkingClientJFrame = sbkingClientJFrame;
        this.sbkingClient = sbkingClient;
    }

    public void run() {
        LOGGER.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbkingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            sleepFor(300);
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
                sleepFor(1000);
                if (sbkingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a player.");
                    }
                    LOGGER.info("Starting to paint Deal");
                    Deal currentDeal = sbkingClient.getDeal();
                    Board currentBoard = sbkingClient.getCurrentBoard();
                    if (currentDeal == null) {
                        paintDeal(currentBoard, sbkingClient.getDirection(), sbkingClient.getPlayCardActionListener());
                    } else {
                        paintDeal(currentDeal, sbkingClient.getDirection(), sbkingClient.getPlayCardActionListener());
                    }
                    LOGGER.info("Finished painting Deal");
                } else {
                    if (!sbkingClient.isPositiveOrNegativeSelected()) {
                        LOGGER.info("Positive or Negative not selected yet!");
                        if (sbkingClient.getDirection() == null || !sbkingClient.isPositiveOrNegativeChooserSet()) {
                            LOGGER.info("Direction not set yet.");
                            LOGGER.info("or Chooser not set yet.");
                            continue;
                        } else {
                            LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            LOGGER.info("My direction: " + sbkingClient.getDirection());
                            LOGGER.info("Chooser: " + sbkingClient.getGameModeOrStrainChooser());
                            paintWaitingForChoosingPositiveOrNegativeScreen(sbkingClient.getDirection(),
                                    sbkingClient.getPositiveOrNegativeChooser());
                            while (!sbkingClient.isPositiveOrNegativeSelected()) {
                                sleepFor(100);
                            }
                        }
                    } else if (!sbkingClient.isRulesetValidSet()) {
                        LOGGER.info("GameModeOrStrain not selected yet!");
                        if (sbkingClient.getDirection() == null || !sbkingClient.isGameModeOrStrainChooserSet()) {
                            LOGGER.info("Direction not set yet.");
                            LOGGER.info("or Chooser not set yet.");
                            continue;
                        } else {
                            LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            LOGGER.info("My direction: " + sbkingClient.getDirection());
                            LOGGER.info("Chooser: " + sbkingClient.getGameModeOrStrainChooser());

                            paintWaitingForChoosingGameModeOrStrainScreen(sbkingClient.getDirection(),
                                    sbkingClient.getGameModeOrStrainChooser(), sbkingClient.isPositive());
                            while (!sbkingClient.isRulesetValidSet()) {
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

    private void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            LOGGER.debug(e);
        }
    }

    private void paintWaitingForChoosingGameModeOrStrainScreen(Direction direction, Direction chooser,
            boolean isPositive) {
        Painter waitingForChoosingGameModeOrStrainPainter = new WaitingForChoosingGameModeOrStrainPainter(direction,
                chooser, isPositive, this.sbkingClient, this.sbkingClient.getCurrentGameScoreboard());
        sbkingClientJFrame.paintPainter(waitingForChoosingGameModeOrStrainPainter);
    }

    private void paintWaitingForChoosingPositiveOrNegativeScreen(Direction direction, Direction chooser) {
        Painter waitingForChoosingPositiveOrNegativePainter = new WaitingForChoosingPositiveOrNegativePainter(direction,
                chooser, this.sbkingClient, this.sbkingClient.getCurrentGameScoreboard());
        sbkingClientJFrame.paintPainter(waitingForChoosingPositiveOrNegativePainter);
    }

    private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
        sbkingClientJFrame.paintPainter(dealPainter);
    }

    private void paintDeal(Board board, Direction direction, ActionListener playCardActionListener) {
        Painter dealPainter = new DealPainter(playCardActionListener, direction, board);
        sbkingClientJFrame.paintPainter(dealPainter);
    }

}
