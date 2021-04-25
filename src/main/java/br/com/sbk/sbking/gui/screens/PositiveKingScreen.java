package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class PositiveKingScreen extends GameScreen {

    public PositiveKingScreen(SBKingClient sbkingClient) {
        super(sbkingClient);
    }

    @Override
    public void runAt(SBKingClientJFrame sbkingClientJFrame) {
        LOGGER.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbkingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            sleepFor(300);
            if (!this.checkIfStillIsOnGameScreen()) {
                return; // Exits when server says it is not on the game anymore.
            }
            if (sbkingClient.isSpectator()) {
                if (sbkingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.info("Deal has changed. Painting deal.");
                        LOGGER.info("It is a spectator.");
                    }
                    Deal currentDeal = sbkingClient.getDeal();
                    if (currentDeal != null) {
                        Painter painter = this.painterFactory.getSpectatorPainter(currentDeal,
                                sbkingClient.getActionListener());
                        sbkingClientJFrame.paintPainter(painter);
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
                    Painter painter = this.painterFactory.getDealPainter(sbkingClient.getDeal(),
                            sbkingClient.getDirection(), sbkingClient.getActionListener());
                    sbkingClientJFrame.paintPainter(painter);
                    LOGGER.info("Finished painting Deal");
                } else {
                    if (!sbkingClient.isRulesetValidSet()) {
                        LOGGER.info("Suit not selected yet!");
                        if (sbkingClient.getDirection() == null || !sbkingClient.isGameModeOrStrainChooserSet()) {
                            LOGGER.info("Direction not set yet.");
                            LOGGER.info("or Chooser not set yet.");
                            continue;
                        } else {
                            LOGGER.info("paintWaitingForChoosingGameModeOrStrainScreen!");
                            LOGGER.info("My direction: " + sbkingClient.getDirection());
                            LOGGER.info("Chooser: " + sbkingClient.getGameModeOrStrainChooser());

                            Painter painter = this.painterFactory.getWaitingForChoosingGameModeOrStrainPainter(
                                    sbkingClient.getDirection(), sbkingClient.getGameModeOrStrainChooser(), true);
                            sbkingClientJFrame.paintPainter(painter);
                            while (!sbkingClient.isRulesetValidSet()) {
                                sleepFor(100);
                            }
                        }
                    }
                }
            }
        }
    }

}
