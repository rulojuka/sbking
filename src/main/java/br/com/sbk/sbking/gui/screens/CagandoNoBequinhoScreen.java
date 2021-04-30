package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class CagandoNoBequinhoScreen extends GameScreen {

    public CagandoNoBequinhoScreen(SBKingClient sbkingClient) {
        super(sbkingClient);
    }

    @Override
    public void runAt(SBKingClientJFrame sbkingClientJFrame) {
        LOGGER.info("Waiting for sbKingClient.isDirectionSet() to be true");
        while (!sbkingClient.isDirectionOrSpectatorSet()) {
            sleepFor(100);
        }

        while (true) {
            if (!this.checkIfStillIsOnGameScreen()) {
                return; // Exits when server says it is not on the game anymore.
            }
            if (sbkingClient.isSpectator()) {
                if (sbkingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.trace("Deal has changed. Painting deal.");
                        LOGGER.trace("It is a spectator.");
                    }
                    Deal currentDeal = sbkingClient.getDeal();
                    if (currentDeal != null) {
                        Painter painter = this.painterFactory.getSpectatorPainter(currentDeal,
                                sbkingClient.getActionListener());
                        sbkingClientJFrame.paintPainter(painter);
                    }
                }
            } else {
                if (sbkingClient.getDealHasChanged() || ClientApplicationState.getGUIHasChanged()) {
                    if (!ClientApplicationState.getGUIHasChanged()) {
                        LOGGER.trace("Deal has changed. Painting deal.");
                        LOGGER.trace("It is a player.");
                    }
                    Deal currentDeal = sbkingClient.getDeal();

                    LOGGER.trace("Starting to paint Deal");
                    Painter painter = this.painterFactory.getDealPainter(currentDeal, sbkingClient.getDirection(),
                            sbkingClient.getActionListener());
                    sbkingClientJFrame.paintPainter(painter);
                    LOGGER.trace("Finished painting Deal");
                }

            }
            sleepFor(300);
        }
    }

}
