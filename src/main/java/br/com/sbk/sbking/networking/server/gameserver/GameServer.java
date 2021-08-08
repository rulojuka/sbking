package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.game.TrickGame;
import br.com.sbk.sbking.networking.server.SBKingServer;
import br.com.sbk.sbking.networking.server.Table;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;

public abstract class GameServer implements Runnable {

    protected CardPlayNotification cardPlayNotification = new CardPlayNotification();
    protected boolean dealHasChanged;
    protected Table table;
    protected SBKingServer sbkingServer = null;

    protected TrickGame game;

    protected int timeoutCardPlayNotification = 1000;

    public void setTable(Table table) {
        this.table = table;
    }

    public void setSBKingServer(SBKingServer sbkingServer) {
        this.sbkingServer = sbkingServer;
    }

    protected void playCard(Card card, Direction direction) {
        LOGGER.info("It is currently the " + this.game.getCurrentDeal().getCurrentPlayer() + " turn");
        try {
            if (this.game.getCurrentDeal().getCurrentPlayer() == direction) {
                syncPlayCard(card);
                this.dealHasChanged = true;
            } else {
                throw new PlayedCardInAnotherPlayersTurnException();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    protected synchronized void syncPlayCard(Card card) {
        LOGGER.trace("Entering synchronized play card");
        this.game.getCurrentDeal().playCard(card);
        LOGGER.trace("Leaving synchronized play card");
    }

    public void notifyPlayCard(Card card, Direction direction) {
        synchronized (cardPlayNotification) {
            LOGGER.trace("Started notifying main thread that I(" + direction + ") want to play the " + card);
            this.releaseAllWaiters(card, direction);
            LOGGER.trace("Finished notifying main thread that I(" + direction + ") want to play the " + card);
        }
    }

    private void releaseAllWaiters(Card card, Direction direction) {
        cardPlayNotification.notifyAllWithCardAndDirection(card, direction);
    }

    protected void sleepFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public Deal getDeal() {
        return this.game.getCurrentDeal();
    }

    protected void sleepToShowLastCard() {
        sleepFor(4000);
    }

    public SBKingServer getSBKingServer() {
        return this.sbkingServer;
    }

    protected void sendDealAll() {
        this.getSBKingServer().sendDealToTable(this.game.getCurrentDeal(), this.table);
    }

    protected void sendInitializeDealAll() {
        this.getSBKingServer().sendInitializeDealToTable(this.table);
    }

    protected void sendInvalidRulesetAll() {
        this.getSBKingServer().sendInvalidRulesetToTable(this.table);
    }

    protected void sendValidRulesetAll() {
        this.getSBKingServer().sendValidRulesetToTable(this.table);
    }

    public void undo(Direction direction) {
        this.getDeal().undo(direction);
    }

    public void claim(Direction direction) {
        this.getDeal().claim(direction);
    }

    protected void copyPlayersFromTableToGame() {
        for (Direction direction : Direction.values()) {
            Player player = this.table.getPlayerOf(direction);
            this.game.setPlayerOf(direction, player);
        }
    }

    protected void copyPlayersFromTableToDeal() {
        for (Direction direction : Direction.values()) {
            this.game.getCurrentDeal().setPlayerOf(direction, this.table.getPlayerOf(direction));
        }
    }

    protected void sleepToShowHands() {
        sleepFor(8000);
    }

    protected void giveBackAllCards() {
        this.getDeal().giveBackAllCardsToHands();
    }

    public void acceptClaim(Direction direction) {
        this.getDeal().acceptClaim(direction);
    }

    public void rejectClaim() {
        this.getDeal().rejectClaim();
    }

    protected void executeCardPlayNotification(CardPlayNotification cardPlayNotification) {
        Direction directionToBePlayed = cardPlayNotification.getDirection();
        if (directionToBePlayed != null) {
            Card cardToBePlayed = cardPlayNotification.getCard();
            LOGGER.info("Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
            try {
                this.playCard(cardToBePlayed, directionToBePlayed);
            } catch (Exception e) {
                throw e;
            }
        }
    }

}
