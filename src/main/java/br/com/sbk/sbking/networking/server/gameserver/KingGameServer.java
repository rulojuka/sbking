package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.KingGame;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class KingGameServer extends GameServer {

    private PositiveOrNegativeNotification positiveOrNegativeNotification;
    private PositiveOrNegative currentPositiveOrNegative;
    private GameModeOrStrainNotification gameModeOrStrainNotification;
    private Ruleset currentGameModeOrStrain;
    private boolean isRulesetPermitted;

    private KingGame kingGame;

    public KingGameServer() {
        this.game = new KingGame();
        this.kingGame = (KingGame) this.game;
    }

    @Override
    public void run() {

        while (!game.isFinished()) {
            this.game.dealNewBoard();

            do {
                this.copyPlayersFromTableToGame();

                this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
                this.positiveOrNegativeNotification = new PositiveOrNegativeNotification();
                this.sendInitializeDealAll();
                LOGGER.info("Sleeping for 300ms waiting for clients to initialize its deals.");
                sleepFor(300);
                this.getSBKingServer().sendDealToTable(this.game.getCurrentDeal(), this.table);
                sleepFor(300);
                this.sendPositiveOrNegativeChooserAll();

                synchronized (positiveOrNegativeNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    try {
                        LOGGER.info(
                                "I am waiting for some thread to notify that it wants to choose positive or negative");
                        positiveOrNegativeNotification.wait(3000);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }

                    while (positiveOrNegativeNotification.getPositiveOrNegative() == null) {
                        try {
                            LOGGER.info(
                                    "I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            positiveOrNegativeNotification.wait(3000);
                            this.sendPositiveOrNegativeChooserAll();
                        } catch (InterruptedException e) {
                            LOGGER.error(e);
                        }
                    }
                }

                LOGGER.info("I received that is going to be "
                        + positiveOrNegativeNotification.getPositiveOrNegative().toString());
                this.currentPositiveOrNegative = positiveOrNegativeNotification.getPositiveOrNegative();
                this.sendPositiveOrNegativeAll();
                sleepFor(300);
                this.sendGameModeOrStrainChooserAll();

                synchronized (gameModeOrStrainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
                        LOGGER.info("getGameModeOrStrain:" + gameModeOrStrainNotification.getGameModeOrStrain());
                        try {
                            LOGGER.info(
                                    "I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            gameModeOrStrainNotification.wait(3000);
                            this.sendPositiveOrNegativeAll();
                            sleepFor(300);
                            this.sendGameModeOrStrainChooserAll();

                        } catch (InterruptedException e) {
                            LOGGER.error(e);
                        }
                    }
                }
                LOGGER.info("I received that is going to be "
                        + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
                this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

                isRulesetPermitted = this.kingGame.isGameModePermitted(this.currentGameModeOrStrain,
                        this.getCurrentGameModeOrStrainChooser());

                if (!isRulesetPermitted) {
                    LOGGER.info("This ruleset is not permitted. Restarting choose procedure");
                    this.sendInvalidRulesetAll();
                } else {
                    this.sendValidRulesetAll();
                }

            } while (!isRulesetPermitted);

            LOGGER.info("Sleeping for 300ms waiting for everything come out right.");
            sleepFor(300);
            LOGGER.info("Everything selected! Game commencing!");
            this.kingGame.addRuleset(currentGameModeOrStrain);

            this.copyPlayersFromTableToDeal();

            this.dealHasChanged = true;
            LOGGER.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
            sleepFor(300);
            while (!this.game.getCurrentDeal().isFinished()) {
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
                        LOGGER.error(e);
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

            LOGGER.info("Sending last 'round' of deals");
            this.sendDealAll();
            LOGGER.info("Sleeping for 3000ms for everyone to see the last card.");
            sleepFor(3000);

            this.giveBackAllCards();
            this.sendDealAll();
            this.sleepToShowHands();

            this.game.finishDeal();

            LOGGER.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
            sleepFor(300);
            this.sbkingServer.sendFinishDealToTable(this.table);
            LOGGER.info("Deal finished!");
            LOGGER.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
            sleepFor(300);
        }

        LOGGER.info("Game has ended.");
    }

    public void notifyChoosePositiveOrNegative(PositiveOrNegative positiveOrNegative, Direction direction) {
        synchronized (positiveOrNegativeNotification) {
            if (this.getCurrentPositiveOrNegativeChooser() == direction) {
                this.positiveOrNegativeNotification.notifyAllWithPositiveOrNegative(positiveOrNegative);
            } else {
                throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
            }
        }
    }

    public void notifyChooseGameModeOrStrain(Ruleset gameModeOrStrain, Direction direction) {
        synchronized (gameModeOrStrainNotification) {
            if (this.getCurrentGameModeOrStrainChooser() == direction) {
                this.gameModeOrStrainNotification.notifyAllWithGameModeOrStrain(gameModeOrStrain);
            } else {
                throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
            }
        }
    }

    private Direction getCurrentPositiveOrNegativeChooser() {
        return this.game.getDealer().getPositiveOrNegativeChooserWhenDealer();
    }

    private Direction getCurrentGameModeOrStrainChooser() {
        return this.game.getDealer().getGameModeOrStrainChooserWhenDealer();
    }

    private void sendGameModeOrStrainChooserAll() {
        this.sbkingServer.sendGameModeOrStrainChooserToTable(this.getCurrentGameModeOrStrainChooser(), this.table);
    }

    private void sendPositiveOrNegativeChooserAll() {
        this.sbkingServer.sendPositiveOrNegativeChooserToTable(this.getCurrentPositiveOrNegativeChooser(), this.table);
    }

    private void sendPositiveOrNegativeAll() {
        this.sbkingServer.sendPositiveOrNegativeToTable(this.currentPositiveOrNegative, this.table);
    }

}
