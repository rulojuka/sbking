package br.com.sbk.sbking.networking.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.TrickGame;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;

public abstract class GameServer implements Runnable {
    static final  Logger logger = LogManager.getLogger(GameServer.class);

    protected CardPlayNotification cardPlayNotification = new CardPlayNotification();
    protected boolean dealHasChanged;
    protected Direction nextDirection = Direction.values()[0];
    protected Table table;

    protected TrickGame game;

    public void setTable(Table table) {
        this.table = table;
    }

    protected void playCard(Card card, Direction direction) {
        logger.info("It is currently the " + this.game.getCurrentDeal().getCurrentPlayer() + " turn");
        try {
            if (this.game.getCurrentDeal().getCurrentPlayer() == direction) {
                syncPlayCard(card);
                this.dealHasChanged = true;
            } else {
                throw new PlayedCardInAnotherPlayersTurnException();
            }
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    protected synchronized void syncPlayCard(Card card) {
        logger.info("Entering synchronized play card");
        this.game.getCurrentDeal().playCard(card);
        logger.info("Leaving synchronized play card");
    }

    public void notifyPlayCard(Card card, Direction direction) {
        synchronized (cardPlayNotification) {
            logger.info("Started notifying main thread that I(" + direction + ") want to play the " + card);
            this.releaseAllWaiters(card, direction);
            logger.info("Finished notifying main thread that I(" + direction + ") want to play the " + card);
        }
    }

    private void releaseAllWaiters(Card card, Direction direction) {
        cardPlayNotification.notifyAllWithCardAndDirection(card, direction);
    }

    protected void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            logger.debug(e);
        }
    }

    public Deal getDeal() {
        return this.game.getCurrentDeal();
    }

    public Board getBoard() {
        return this.game.getCurrentBoard();
    }

    protected void sleepToShowLastCard() {
        sleepFor(4000);
    }

}
