package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.DealWithDummyPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingGameModeOrStrainPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

@SuppressWarnings("serial")
public class MinibridgeScreen {

  private SBKingClient sbKingClient;
  private SBKingClientJFrame sbkingClientJFrame;

  public MinibridgeScreen(SBKingClientJFrame sbkingClientJFrame, SBKingClient sbKingClient) {
    this.sbkingClientJFrame = sbkingClientJFrame;
    this.sbKingClient = sbKingClient;
  }

  public void run() {
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
        sleepFor(100);
        if (sbKingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
          if (!ClientApplicationState.getGUIHasChanged()) {
            LOGGER.info("Deal has changed. Painting deal.");
            LOGGER.info("It is a player.");
          }
          LOGGER.info("Starting to paint Deal");
          paintDeal(sbKingClient.getDeal(), sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
          LOGGER.info("Finished painting Deal");
        } else {
          if (!sbKingClient.isRulesetValidSet()) {
            LOGGER.info("Suit not selected yet!");
            if (sbKingClient.getDirection() == null || !sbKingClient.isGameModeOrStrainChooserSet()) {
              LOGGER.info("Direction not set yet.");
              LOGGER.info("or Chooser not set yet.");
              continue;
            } else {
              LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
              LOGGER.info("My direction: " + sbKingClient.getDirection());
              LOGGER.info("Chooser: " + sbKingClient.getGameModeOrStrainChooser());

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

  private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
    boolean dummyVisible = this.sbKingClient.getDeal().isDummyOpen();
    Direction dummy = this.sbKingClient.getDeal().getDummy();
    Painter dealPainter = new DealWithDummyPainter(playCardActionListener, direction, deal, dummy, dummyVisible);
    sbkingClientJFrame.paintPainter(dealPainter);
  }

  private void paintWaitingForChoosingGameModeOrStrainScreen(Direction direction, Direction chooser,
      boolean isPositive) {
    Painter waitingForChoosingGameModeOrStrainPainter = new WaitingForChoosingGameModeOrStrainPainter(direction,
        chooser, true, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
    sbkingClientJFrame.paintPainter(waitingForChoosingGameModeOrStrainPainter);
  }
}
