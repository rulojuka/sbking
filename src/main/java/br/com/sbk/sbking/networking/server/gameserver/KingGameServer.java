package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.game.KingGame;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;
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
        super();
        Deque<Card> deck = new Complete52CardDeck().getDeck();
        this.game = new KingGame(deck);
        this.kingGame = (KingGame) this.game;
    }

    @SuppressWarnings("checkstyle:methodlength")
    @Override
    public void run() {

        while (!shouldStop && !game.isFinished()) {
            this.game.dealNewBoard();

            do {
                this.copyPlayersFromTableToGame();

                initializeNotifications();
                this.sendInitializeDealAll();
                waitForClientsToPrepare();
                if (this.shouldStop) {
                    return;
                }
                this.getSBKingServer().sendDealToTable(this.game.getCurrentDeal(), this.table);
                if (this.shouldStop) {
                    return;
                }
                waitForClientsToPrepare();
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
                    if (this.shouldStop) {
                        return;
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
                    if (this.shouldStop) {
                        return;
                    }
                }

                LOGGER.info("I received that is going to be "
                        + positiveOrNegativeNotification.getPositiveOrNegative().toString());
                this.currentPositiveOrNegative = positiveOrNegativeNotification.getPositiveOrNegative();
                this.sendPositiveOrNegativeAll();
                if (this.shouldStop) {
                    return;
                }
                waitForClientsToPrepare();
                this.sendGameModeOrStrainChooserAll();

                synchronized (gameModeOrStrainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
                        LOGGER.debug("getGameModeOrStrain: {}", gameModeOrStrainNotification.getGameModeOrStrain());
                        try {
                            LOGGER.debug(
                                    "I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            gameModeOrStrainNotification.wait(3000);
                            this.sendPositiveOrNegativeAll();
                            waitForClientsToPrepare();
                            this.sendGameModeOrStrainChooserAll();

                        } catch (InterruptedException e) {
                            LOGGER.error(e);
                        }
                    }
                }
                if (this.shouldStop) {
                    return;
                }
                LOGGER.info("I received that is going to be "
                        + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
                this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

                isRulesetPermitted = this.kingGame.isGameModePermitted(this.currentGameModeOrStrain,
                        this.getCurrentGameModeOrStrainChooser());

                if (!isRulesetPermitted) {
                    LOGGER.warn("This ruleset is not permitted. Restarting choose procedure");
                    this.sendInvalidRulesetAll();
                } else {
                    this.sendValidRulesetAll();
                }

            } while (!shouldStop && !isRulesetPermitted);

            if (this.shouldStop) {
                return;
            }
            LOGGER.info("Everything selected! Game commencing!");
            this.kingGame.setRuleset(currentGameModeOrStrain);

            this.copyPlayersFromTableToDeal();

            this.dealHasChanged = true;
            waitForClientsToPrepare();
            while (!shouldStop && !this.game.getCurrentDeal().isFinished()) {
                if (this.dealHasChanged) {
                    LOGGER.debug("Sending new 'round' of deals");
                    this.sendDealAll();
                    this.dealHasChanged = false;
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
                if (this.shouldStop) {
                    return;
                }
                this.executeCardPlayNotification(cardPlayNotification);
                cardPlayNotification = new CardPlayNotification();
            }
            if (this.shouldStop) {
                return;
            }

            LOGGER.info("Sending last 'round' of deals");
            this.sendDealAll();
            LOGGER.info("Sleeping for 3000ms for everyone to see the last card.");
            sleepFor(3000);
            if (this.shouldStop) {
                return;
            }

            this.giveBackAllCards();
            this.sendDealAll();
            this.sleepToShowHands();

            this.game.finishDeal();

            if (this.shouldStop) {
                return;
            }
            waitForClientsToPrepare();
            this.sbkingServer.sendFinishDealToTable(this.table);
            LOGGER.info("Deal finished!");
            waitForClientsToPrepare();
            if (this.shouldStop) {
                return;
            }
        }

        LOGGER.info("Game has ended.");
    }

    private void initializeNotifications() {
        this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
        this.positiveOrNegativeNotification = new PositiveOrNegativeNotification();
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
