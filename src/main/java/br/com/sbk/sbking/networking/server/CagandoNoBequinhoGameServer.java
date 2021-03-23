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

    final static Logger logger = LogManager.getLogger(CagandoNoBequinhoGameServer.class);

    private CagandoNoBequinhoGame cagandoNoBequinhoGame;

    public CagandoNoBequinhoGameServer() {
        this.game = new CagandoNoBequinhoGame();
    }

    @Override
    public void run() {

        logger.info("Sleeping for 500ms waiting for last client to setup itself");
        sleepFor(500);

        this.cagandoNoBequinhoGame = (CagandoNoBequinhoGame) this.game;

        while (!game.isFinished()) {

            this.cagandoNoBequinhoGame.dealNewBoard();

            for (Direction direction : Direction.values()) {
                Player player = this.table.getPlayerOf(direction);
                this.game.setPlayerOf(direction, player);
            }

            logger.info("Sleeping for 300ms waiting for everything come out right.");
            sleepFor(300);

            logger.info("Everything selected! Game commencing!");

            Deal currentDeal = this.game.getCurrentDeal();
            for (Direction direction : Direction.values()) {
                currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
            }

            this.dealHasChanged = true;
            while (!this.game.getCurrentDeal().isFinished()) {
                logger.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
                sleepFor(300);
                if (this.dealHasChanged) {
                    logger.info("Sending new 'round' of deals");
                    this.table.getMessageSender().sendDealAll(this.game.getCurrentDeal());
                    this.dealHasChanged = false;
                }
                synchronized (cardPlayNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    try {
                        logger.info("I am waiting for some thread to notify that it wants to play a card.");
                        cardPlayNotification.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Direction directionToBePlayed = cardPlayNotification.getDirection();
                Card cardToBePlayed = cardPlayNotification.getCard();
                logger.info("Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
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
            logger.info("Deal finished!");

        }

        this.table.getMessageSender().sendFinishGameAll();

        logger.info("Game has ended.");

    }

}
