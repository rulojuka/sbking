package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.game.MinibridgeGame;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;

public class MinibridgeGameServer extends GameServer {

  private GameModeOrStrainNotification gameModeOrStrainNotification;
  private Ruleset currentGameModeOrStrain;
  private boolean isRulesetPermitted;

  private MinibridgeGame minibridgeGame;

  public MinibridgeGameServer() {
    this.setGameWithCardDeck(new Complete52CardDeck().getDeck());
  }

  @Override
  public void run() {

    while (!shouldStop && !game.isFinished()) {
      this.game.dealNewBoard();

      do {
        this.copyPlayersFromTableToGame();
        this.copyPlayersFromTableToDeal();
        this.sendInitializeDealAll();
        this.sendDealAll();

        this.gameModeOrStrainNotification = new GameModeOrStrainNotification();
        synchronized (gameModeOrStrainNotification) {
          // wait until object notifies - which relinquishes the lock on the object too
          while (!shouldStop && gameModeOrStrainNotification.getGameModeOrStrain() == null) {
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
        if (this.shouldStop) {
          return;
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

      } while (!shouldStop && !isRulesetPermitted);
      if (this.shouldStop) {
        return;
      }

      LOGGER.info("Everything selected! Game commencing!");
      this.minibridgeGame.setRuleset(currentGameModeOrStrain);

      if (currentGameModeOrStrain instanceof PositiveWithTrumpsRuleset) {
        PositiveWithTrumpsRuleset positiveWithTrumpsRuleset = (PositiveWithTrumpsRuleset) currentGameModeOrStrain;
        this.game.getCurrentDeal().sortAllHandsByTrumpSuit(positiveWithTrumpsRuleset.getTrumpSuit());
      }

      this.dealHasChanged = true;
      while (!shouldStop && !this.game.getCurrentDeal().isFinished()) {
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
      if (this.shouldStop) {
        return;
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

  protected void setGameWithCardDeck(Deque<Card> deck) {
    this.game = new MinibridgeGame(deck);
    this.minibridgeGame = (MinibridgeGame) this.game;
  }

}
