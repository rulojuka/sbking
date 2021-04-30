package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.MinibridgeGame;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;

public class MinibridgeGameServer extends GameServer {

  private GameModeOrStrainNotification gameModeOrStrainNotification;
  private Ruleset currentGameModeOrStrain;
  private boolean isRulesetPermitted;

  private MinibridgeGame minibridgeGame;

  public MinibridgeGameServer() {
    this.game = new MinibridgeGame();
    this.minibridgeGame = (MinibridgeGame) this.game;
  }

  @Override
  public void run() {

    while (!game.isFinished()) {
      this.game.dealNewBoard();

      do {
        this.copyPlayersFromTableToGame();
        this.copyPlayersFromTableToDeal();
        this.sendInitializeDealAll();
        this.sendDealAll();

        this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
        synchronized (gameModeOrStrainNotification) {
          // wait until object notifies - which relinquishes the lock on the object too
          while (gameModeOrStrainNotification.getGameModeOrStrain() == null) {
            LOGGER.debug("getGameModeOrStrain:" + gameModeOrStrainNotification.getGameModeOrStrain());
            try {
              LOGGER.debug("I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
              gameModeOrStrainNotification.wait(3000);
              this.sendGameModeOrStrainChooserAll();
            } catch (InterruptedException e) {
              LOGGER.error(e);
            }
          }
        }
        LOGGER.info("I received that is going to be "
            + gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
        this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

        isRulesetPermitted = this.minibridgeGame.isGameModePermitted(this.currentGameModeOrStrain,
            this.getCurrentGameModeOrStrainChooser());

        if (!isRulesetPermitted) {
          LOGGER.warn("This ruleset is not permitted. Restarting choose procedure");
          this.sendInvalidRulesetAll();
        } else {
          this.sendValidRulesetAll();
        }

      } while (!isRulesetPermitted);

      LOGGER.info("Everything selected! Game commencing!");
      this.minibridgeGame.addRuleset(currentGameModeOrStrain);

      if (currentGameModeOrStrain instanceof PositiveWithTrumpsRuleset) {
        PositiveWithTrumpsRuleset positiveWithTrumpsRuleset = (PositiveWithTrumpsRuleset) currentGameModeOrStrain;
        this.game.getCurrentDeal().sortAllHandsByTrumpSuit(positiveWithTrumpsRuleset.getTrumpSuit());
      }

      this.dealHasChanged = true;
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
        LOGGER.info("Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
        try {
          this.playCard(cardToBePlayed, directionToBePlayed);
        } catch (Exception e) {
          throw e;
        }
      }

      this.sendDealAll();
      this.sleepToShowLastCard();

      this.giveBackAllCards();
      this.sendDealAll();
      this.sleepToShowHands();

      this.game.finishDeal();

      this.sbkingServer.sendFinishDealToTable(this.table);
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
      LOGGER.info(e);
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
    this.sbkingServer.sendGameModeOrStrainChooserToTable(this.getCurrentGameModeOrStrainChooser(), this.table);
  }

}
