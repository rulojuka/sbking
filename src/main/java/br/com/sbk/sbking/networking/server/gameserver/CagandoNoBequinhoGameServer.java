package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.game.CagandoNoBequinhoGame;

public class CagandoNoBequinhoGameServer extends GameServer {

    public CagandoNoBequinhoGameServer() {
        this.game = new CagandoNoBequinhoGame();
    }

    @Override
    public void run() {

        while (!game.isFinished()) {
            this.game.dealNewBoard();

            this.game.dealNewBoard();

            this.copyPlayersFromTableToGame();

            LOGGER.info("Everything selected! Game commencing!");

            this.copyPlayersFromTableToDeal();

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
                        cardPlayNotification.wait(this.timeoutCardPlayNotification);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }
                }
                this.executeCardPlayNotification(cardPlayNotification);
            }

            this.sendDealAll();
            this.sleepToShowLastCard();

            this.giveBackAllCards();
            this.sendDealAll();
            this.sleepToShowHands();

            this.game.finishDeal();

            this.sbkingServer.sendFinishDealToTable(this.table);
            LOGGER.info("Deal finished!");

        }

        LOGGER.info("Game has ended.");

    }

}
