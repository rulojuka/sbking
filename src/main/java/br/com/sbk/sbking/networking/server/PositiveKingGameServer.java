package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.networking.utils.SleepUtils.sleepFor;
import static br.com.sbk.sbking.networking.utils.SleepUtils.sleepForWithInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.PositiveKingGame;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class PositiveKingGameServer extends GameServer {

    private static final Logger LOGGER = LogManager.getLogger(PositiveKingGameServer.class);

    private PositiveOrNegativeNotification positiveOrNegativeNotification;
    private GameModeOrStrainNotification gameModeOrStrainNotification;
    private Ruleset currentGameModeOrStrain;
    private boolean isRulesetPermitted;

    private PositiveKingGame positiveKingGame;

    public PositiveKingGameServer() {
        this.game = new PositiveKingGame();
    }

    @Override
    public void run() {

        sleepForWithInfo(500, LOGGER, "Waiting for clients to setup themselves.");

        this.game = new PositiveKingGame();
        this.positiveKingGame = (PositiveKingGame) this.game;

        while (!game.isFinished()) {
            this.game.dealNewBoard();

            for (Direction direction : Direction.values()) {
                Player player = this.table.getPlayerOf(direction);
                this.game.setPlayerOf(direction, player);
            }

            do {
                this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
                this.positiveOrNegativeNotification = new PositiveOrNegativeNotification();
                sleepForWithInfo(300, LOGGER, "Waiting for clients to initialize its deals.");

                LOGGER.info("Everything selected! Game commencing!");

                Deal currentDeal = this.game.getCurrentDeal();
                for (Direction direction : Direction.values()) {
                    currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
                }

                this.table.getMessageSender().sendInitializeDealAll();
                this.table.getMessageSender().sendBoardAll(this.game.getCurrentBoard());
                sleepFor(200, LOGGER);
                this.table.getMessageSender().sendDealAll(this.game.getCurrentDeal());

                PositiveOrNegative positive = new PositiveOrNegative();
                positive.setPositive();

                synchronized (gameModeOrStrainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
                        LOGGER.info("getGameModeOrStrain:" + gameModeOrStrainNotification.getGameModeOrStrain());
                        try {
                            LOGGER.info(
                                    "I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            gameModeOrStrainNotification.wait(3000);
                            this.table.getMessageSender()
                                    .sendChooserGameModeOrStrainAll(this.getCurrentGameModeOrStrainChooser());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                LOGGER.info("I received that is going to be "
                        + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
                this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

                isRulesetPermitted = this.positiveKingGame.isGameModePermitted(this.currentGameModeOrStrain,
                        this.getCurrentGameModeOrStrainChooser());

                if (!isRulesetPermitted) {
                    LOGGER.info("This ruleset is not permitted. Restarting choose procedure");
                    this.table.getMessageSender().sendInvalidRulesetAll();
                } else {
                    this.table.getMessageSender().sendValidRulesetAll();
                }

            } while (!isRulesetPermitted);

            this.table.getMessageSender()
                    .sendGameModeOrStrainShortDescriptionAll(this.currentGameModeOrStrain.getShortDescription());

            sleepForWithInfo(300, LOGGER, "Waiting for everything come out right.");

            LOGGER.info("Everything selected! Game commencing!");
            this.positiveKingGame.addRuleset(currentGameModeOrStrain);

            Deal currentDeal = this.game.getCurrentDeal();
            for (Direction direction : Direction.values()) {
                currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
            }

            this.dealHasChanged = true;
            while (!this.game.getCurrentDeal().isFinished()) {
                sleepForWithInfo(300, LOGGER, "Waiting for all clients to prepare themselves.");
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
            this.table.getMessageSender().sendGameScoreboardAll(this.positiveKingGame.getGameScoreboard());

            this.table.getMessageSender().sendFinishDealAll();
            LOGGER.info("Deal finished!");

        }

        this.table.getMessageSender().sendFinishGameAll();

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

}
