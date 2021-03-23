package br.com.sbk.sbking.networking.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.MinibridgeGame;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;

public class MinibridgeGameServer extends GameServer {

  static final Logger logger = LogManager.getLogger(MinibridgeGameServer.class);

  private GameModeOrStrainNotification gameModeOrStrainNotification;
  private Ruleset currentGameModeOrStrain;
  private boolean isRulesetPermitted;

  private MinibridgeGame minibridgeGame;

  public MinibridgeGameServer() {
    this.game = new MinibridgeGame();
  }

  @Override
  public void run() {

    logger.info("Sleeping for 500ms waiting for clients to setup themselves");
    sleepFor(500);

    this.game = new MinibridgeGame();
    this.minibridgeGame = (MinibridgeGame) this.game;

    while (!game.isFinished()) {
      this.game.dealNewBoard();

      for (Direction direction : Direction.values()) {
        Player player = this.table.getPlayerOf(direction);
        this.game.setPlayerOf(direction, player);
      }

      logger.info("Sleeping for 300ms waiting for clients to initialize its deals.");
      sleepFor(300);

      logger.info("Everything selected! Game commencing!");

      Deal currentDeal = this.game.getCurrentDeal();
      for (Direction direction : Direction.values()) {
        currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
      }
      do {
        this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
        logger.info("Sleeping for 300ms waiting for clients to initialize its deals.");
        sleepFor(300);

        logger.info("Everything selected! Game commencing!");

        currentDeal = this.game.getCurrentDeal();
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

        isRulesetPermitted = this.minibridgeGame.isGameModePermitted(this.currentGameModeOrStrain,
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
      this.minibridgeGame.addRuleset(currentGameModeOrStrain);

      currentDeal = this.game.getCurrentDeal();
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

      this.table.getMessageSender().sendFinishDealAll();
      logger.info("Deal finished!");
    }

    this.table.getMessageSender().sendFinishGameAll();
    logger.info("Game has ended.");
  }

  @Override
  protected void playCard(Card card, Direction direction) {
    Direction currentDirectionToPlay = this.game.getCurrentDeal().getCurrentPlayer();
    logger.info("It is currently the " + currentDirectionToPlay + " turn");
    try {
      if (this.isAllowedToPlayCardInTurnOf(direction, currentDirectionToPlay)) {
        syncPlayCard(card);
        this.dealHasChanged = true;
      } else {
        throw new PlayedCardInAnotherPlayersTurnException();
      }
    } catch (Exception e) {
      logger.debug(e);
    }
  }

  private boolean isAllowedToPlayCardInTurnOf(Direction player, Direction currentTurn) {
    Direction declarer = this.minibridgeGame.getDeclarer();
    Direction dummy = this.minibridgeGame.getDummy();
    if (currentTurn == dummy) {
      return player == declarer;
    } else {
      return player == currentTurn;
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

  private Direction getCurrentGameModeOrStrainChooser() {
    return this.minibridgeGame.getDeclarer();
  }

}
