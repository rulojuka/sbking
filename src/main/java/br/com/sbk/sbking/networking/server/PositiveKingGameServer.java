package br.com.sbk.sbking.networking.server;

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

    final static Logger logger = LogManager.getLogger(KingGameServer.class);

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

        logger.info("Sleeping for 500ms waiting for clients to setup themselves");
        sleepFor(500);

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
                logger.info("Sleeping for 300ms waiting for clients to initialize its deals.");
                sleepFor(300);

                logger.info("Everything selected! Game commencing!");

                Deal currentDeal = this.game.getCurrentDeal();
                for (Direction direction : Direction.values()) {
                    currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
                }

                this.table.getMessageSender().sendInitializeDealAll();
                this.table.getMessageSender().sendBoardAll(this.game.getCurrentBoard());
                sleepFor(200);
                this.table.getMessageSender().sendDealAll(this.game.getCurrentDeal());

                PositiveOrNegative positive = new PositiveOrNegative();
                positive.setPositive();

                synchronized (gameModeOrStrainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
                        logger.info("getGameModeOrStrain:" + gameModeOrStrainNotification.getGameModeOrStrain());
                        try {
                            logger.info("I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            gameModeOrStrainNotification.wait(3000);
                            this.table.getMessageSender().sendChooserGameModeOrStrainAll(this.getCurrentGameModeOrStrainChooser());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                logger.info("I received that is going to be "
                        + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
                this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

                isRulesetPermitted = this.positiveKingGame.isGameModePermitted(this.currentGameModeOrStrain,
                        this.getCurrentGameModeOrStrainChooser());

                if (!isRulesetPermitted) {
                    logger.info("This ruleset is not permitted. Restarting choose procedure");
                    this.table.getMessageSender().sendInvalidRulesetAll();
                } else {
                    this.table.getMessageSender().sendValidRulesetAll();
                }

            } while (!isRulesetPermitted);

            this.table.getMessageSender()
                    .sendGameModeOrStrainShortDescriptionAll(this.currentGameModeOrStrain.getShortDescription());

            logger.info("Sleeping for 300ms waiting for everything come out right.");
            sleepFor(300);

            logger.info("Everything selected! Game commencing!");
            this.positiveKingGame.addRuleset(currentGameModeOrStrain);

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
            this.table.getMessageSender().sendGameScoreboardAll(this.positiveKingGame.getGameScoreboard());

            this.table.getMessageSender().sendFinishDealAll();
            logger.info("Deal finished!");

        }

        this.table.getMessageSender().sendFinishGameAll();

        logger.info("Game has ended.");

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
