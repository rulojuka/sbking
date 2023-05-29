package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.game.CagandoNoBequinhoGame;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;

public class CagandoNoBequinhoGameServer extends GameServer {

    public CagandoNoBequinhoGameServer() {
        super();
        Deque<Card> deck = new Complete52CardDeck().getDeck();
        this.game = new CagandoNoBequinhoGame(deck);
    }

    @Override
    public void run() {

        while (!shouldStop && !game.isFinished()) {
            this.game.dealNewBoard();

            this.game.dealNewBoard();

            this.copyPlayersFromTableToGame();

            LOGGER.info("Everything selected! Game commencing!");

            this.copyPlayersFromTableToDeal();

            this.dealHasChanged = true;
            while (!shouldStop && !this.game.getCurrentDeal().isFinished()) {
                if (this.dealHasChanged) {
                    waitForClientsToPrepare();
                    LOGGER.info("Sending new 'round' of deals");
                    this.sendDealAll();
                    this.dealHasChanged = false;
                }
                if (shouldStop) {
                    return;
                }
                synchronized (cardPlayNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    try {
                        LOGGER.trace("I am waiting for some thread to notify that it wants to play a card.");
                        cardPlayNotification.wait(this.timeoutCardPlayNotification);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }
                }
                if (shouldStop) {
                    return;
                }
                this.executeCardPlayNotification(cardPlayNotification);
                cardPlayNotification = new CardPlayNotification();
            }

            if (shouldStop) {
                return;
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
