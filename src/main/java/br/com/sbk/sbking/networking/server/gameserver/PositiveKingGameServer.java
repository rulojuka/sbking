package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.game.PositiveKingGame;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class PositiveKingGameServer extends GameServer {

    private PositiveOrNegativeNotification positiveOrNegativeNotification;
    private GameModeOrStrainNotification gameModeOrStrainNotification;
    private Ruleset currentGameModeOrStrain;
    private boolean isRulesetPermitted;

    private PositiveKingGame positiveKingGame;

    public PositiveKingGameServer() {
        super();
        Deque<Card> deck = new Complete52CardDeck().getDeck();
        this.game = new PositiveKingGame(deck);
        this.positiveKingGame = (PositiveKingGame) this.game;
    }

    @Override
    public void run() {

        while (!shouldStop && !game.isFinished()) {
            this.game.dealNewBoard();

            this.copyPlayersFromTableToGame();

            do {
                this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
                this.positiveOrNegativeNotification = new PositiveOrNegativeNotification();
                waitForClientsToPrepare();
                if (this.shouldStop) {
                    return;
                }

                LOGGER.info("Everything selected! Game commencing!");

                this.copyPlayersFromTableToDeal();

                this.sendInitializeDealAll();
                if (this.shouldStop) {
                    return;
                }
                waitForClientsToPrepare();
                this.sendDealAll();

                synchronized (gameModeOrStrainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (!shouldStop && gameModeOrStrainNotification.getGameModeOrStrain() == null) {
                        LOGGER.debug("getGameModeOrStrain: {}", gameModeOrStrainNotification.getGameModeOrStrain());
                        try {
                            LOGGER.info(
                                    "I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            gameModeOrStrainNotification.wait(3000);
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

                isRulesetPermitted = this.positiveKingGame.isGameModePermitted(this.currentGameModeOrStrain,
                        this.getCurrentGameModeOrStrainChooser());

                if (!isRulesetPermitted) {
                    LOGGER.warn("This ruleset is not permitted. Restarting choose procedure");
                    this.sendInvalidRulesetAll();
                } else {
                    this.sendValidRulesetAll();
                }

            } while (!shouldStop && !isRulesetPermitted);

            waitForClientsToPrepare();
            if (this.shouldStop) {
                return;
            }

            LOGGER.info("Everything selected! Game commencing!");
            this.positiveKingGame.setRuleset(currentGameModeOrStrain);

            this.copyPlayersFromTableToDeal();

            this.dealHasChanged = true;
            while (!shouldStop && !this.game.getCurrentDeal().isFinished()) {
                waitForClientsToPrepare();
                if (this.shouldStop) {
                    return;
                }
                if (this.dealHasChanged) {
                    LOGGER.info("Sending new 'round' of deals");
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

            this.sendDealAll();
            this.sleepToShowLastCard();
            if (this.shouldStop) {
                return;
            }

            this.giveBackAllCards();
            this.sendDealAll();
            this.sleepToShowHands();
            if (this.shouldStop) {
                return;
            }

            this.game.finishDeal();

            this.sbkingServer.sendFinishDealToTable(this.table);
            LOGGER.info("Deal finished!");

        }
        if (this.shouldStop) {
            return;
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

}
