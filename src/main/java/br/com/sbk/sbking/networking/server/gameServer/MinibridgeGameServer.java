package br.com.sbk.sbking.networking.server.gameServer;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.MinibridgeGame;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;

public class MinibridgeGameServer extends GameServer {

  private GameModeOrStrainNotification gameModeOrStrainNotification;
  private Ruleset currentGameModeOrStrain;
  private boolean isRulesetPermitted;

  private MinibridgeGame minibridgeGame;

  public MinibridgeGameServer() {
    this.game = new MinibridgeGame();
  }

  @Override
  public void run() {

    LOGGER.info("Sleeping for 500ms waiting for clients to setup themselves");
    sleepFor(500);

    LOGGER.info("Sleeping while nobody is connected");
    while (this.sbkingServer.nobodyIsConnected()) {
      sleepFor(500);
    }
    LOGGER.info("Finished sleeping because someone is connected");

    this.game = new MinibridgeGame();
    this.minibridgeGame = (MinibridgeGame) this.game;

    while (!game.isFinished()) {
      this.game.dealNewBoard();

      for (Direction direction : Direction.values()) {
        Player player = this.table.getPlayerOf(direction);
        this.game.setPlayerOf(direction, player);
      }

      LOGGER.info("Sleeping for 300ms waiting for clients to initialize its deals.");
      sleepFor(300);

      LOGGER.info("Everything selected! Game commencing!");

      Deal currentDeal = this.game.getCurrentDeal();
      for (Direction direction : Direction.values()) {
        currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
      }
      do {
        this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
        LOGGER.info("Sleeping for 300ms waiting for clients to initialize its deals.");
        sleepFor(300);

        LOGGER.info("Everything selected! Game commencing!");

        currentDeal = this.game.getCurrentDeal();
        for (Direction direction : Direction.values()) {
          currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
        }

        this.sendInitializeDealAll();
        this.getSBKingServer().sendBoardAll(this.game.getCurrentBoard());
        sleepFor(200);
        this.sendDealAll();

        synchronized (gameModeOrStrainNotification) {
          // wait until object notifies - which relinquishes the lock on the object too
          while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
            LOGGER.info("getGameModeOrStrain:" + gameModeOrStrainNotification.getGameModeOrStrain());
            try {
              LOGGER.info("I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
              gameModeOrStrainNotification.wait(3000);
              this.sendGameModeOrStrainChooserAll();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
        LOGGER.info("I received that is going to be "
            + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
        this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

        isRulesetPermitted = this.minibridgeGame.isGameModePermitted(this.currentGameModeOrStrain,
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
      this.minibridgeGame.addRuleset(currentGameModeOrStrain);
      currentDeal = this.game.getCurrentDeal();

      if (currentGameModeOrStrain instanceof PositiveWithTrumpsRuleset) {
        PositiveWithTrumpsRuleset positiveWithTrumpsRuleset = (PositiveWithTrumpsRuleset) currentGameModeOrStrain;
        currentDeal.sortAllHandsByTrumpSuit(positiveWithTrumpsRuleset.getTrumpSuit());
      }

      for (Direction direction : Direction.values()) {
        currentDeal.setPlayerOf(direction, this.table.getPlayerOf(direction));
      }

      this.dealHasChanged = true;
      while (!this.game.getCurrentDeal().isFinished()) {
        LOGGER.info("Sleeping for 300ms waiting for all clients to prepare themselves.");
        sleepFor(300);
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
            e.printStackTrace();
          }
        }
        Direction directionToBePlayed = cardPlayNotification.getDirection();
        Card cardToBePlayed = cardPlayNotification.getCard();
        LOGGER.info("Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
        try {
          this.playCard(cardToBePlayed, directionToBePlayed);
        } catch (Exception e) {
          throw e;
        }
      }

      this.sendDealAll();
      this.sleepToShowLastCard();

      this.game.finishDeal();

      this.sbkingServer.sendFinishDealAll();
      LOGGER.info("Deal finished!");
    }

    LOGGER.info("Game has ended.");
  }

  @Override
  protected void playCard(Card card, Direction direction) {
    Direction currentDirectionToPlay = this.game.getCurrentDeal().getCurrentPlayer();
    LOGGER.info("It is currently the " + currentDirectionToPlay + " turn");
    try {
      if (this.isAllowedToPlayCardInTurnOf(direction, currentDirectionToPlay)) {
        syncPlayCard(card);
        this.dealHasChanged = true;
      } else {
        throw new PlayedCardInAnotherPlayersTurnException();
      }
    } catch (Exception e) {
      LOGGER.debug(e);
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

  private void sendGameModeOrStrainChooserAll() {
    this.sbkingServer.sendGameModeOrStrainChooserAll(this.getCurrentGameModeOrStrainChooser());
  }

}
