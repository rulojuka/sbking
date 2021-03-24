package br.com.sbk.sbking.networking.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.CagandoNoBequinhoGame;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class CagandoNoBequinhoGameServer extends GameServer {

    private static final Logger LOGGER = LogManager.getLogger(CagandoNoBequinhoGameServer.class);

    private CagandoNoBequinhoGame cagandoNoBequinhoGame;

    public CagandoNoBequinhoGameServer() {
        this.game = new CagandoNoBequinhoGame();
    }

    @Override
    public void run() {

        LOGGER.info("Sleeping for 500ms waiting for last client to setup itself");
        sleepFor(500);

        this.cagandoNoBequinhoGame = (CagandoNoBequinhoGame) this.game;

        while (!game.isFinished()) {

            this.cagandoNoBequinhoGame.dealNewBoard();

            for (Direction direction : Direction.values()) {
                Player player = this.table.getPlayerOf(direction);
                this.game.setPlayerOf(direction, player);
            }

            LOGGER.info("Sleeping for 300ms waiting for everything come out right.");
            sleepFor(300);

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
                    this.table.getMessageSender().sendDealAll(this.game.getCurrentDeal());
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

            this.table.getMessageSender().sendDealAll(this.game.getCurrentDeal());
            this.sleepToShowLastCard();

            this.game.finishDeal();
            this.table.getMessageSender().sendGameScoreboardAll(new KingGameScoreboard());

            this.table.getMessageSender().sendFinishDealAll();
            LOGGER.info("Deal finished!");

        }

        this.table.getMessageSender().sendFinishGameAll();

        LOGGER.info("Game has ended.");

    }

}
