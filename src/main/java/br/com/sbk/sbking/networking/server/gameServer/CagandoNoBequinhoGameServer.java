package br.com.sbk.sbking.networking.server.gameServer;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.CagandoNoBequinhoGame;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class CagandoNoBequinhoGameServer extends GameServer {

    public CagandoNoBequinhoGameServer() {
        this.game = new CagandoNoBequinhoGame();
    }

    @Override
    public void run() {

        while (!game.isFinished()) {

            this.initializePlayers();

            LOGGER.info("Everything selected! Game commencing!");

            Deal currentDeal = this.game.getCurrentDeal();
            for (Direction direction : Direction.values()) {
                currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
            }

            this.dealHasChanged = true;
            while (!this.game.getCurrentDeal().isFinished()) {
                LOGGER.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
                sleepFor(300);
                if (this.dealHasChanged) {
                    LOGGER.info("Sending new 'round' of deals");
                    this.sendDealAll();
                    this.dealHasChanged = false;
                }
                synchronized (cardPlayNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    try {
                        LOGGER.info("I am waiting for some thread to notify that it wants to play a card.");
                        cardPlayNotification.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Direction directionToBePlayed = cardPlayNotification.getDirection();
                Card cardToBePlayed = cardPlayNotification.getCard();
                LOGGER.info(
                        "Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
                try {
                    this.playCard(cardToBePlayed, directionToBePlayed);
                } catch (Exception e) {
                    throw e;
                }
            }

            this.sendDealAll();
            this.sleepToShowLastCard();

            this.game.finishDeal();

            this.sbkingServer.sendFinishDealAll();
            LOGGER.info("Deal finished!");

        }

        LOGGER.info("Game has ended.");

    }

}
