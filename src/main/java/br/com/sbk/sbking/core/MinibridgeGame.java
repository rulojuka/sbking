package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;

public class MinibridgeGame extends TrickGame {

  public MinibridgeGame() {
    super();
    this.dealNewBoard();
  }

  @Override
  public void dealNewBoard() {
    BoardDealer boardDealer = new MinibridgeBoardDealer();
    this.currentBoard = boardDealer.dealBoard(this.dealer);
    this.currentDeal = new Deal(currentBoard, new NoRuleset());
    this.currentDeal.setCurrentPlayer(this.dealer.next());
    this.currentDeal.setDummy(this.dealer.next(2));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void finishDeal() {
    this.dealer = this.dealer.next();
  }

  public Direction getDeclarer() {
    return this.dealer;
  }

  public Direction getDummy() {
    return this.currentDeal.getDummy();
  }

  public boolean isGameModePermitted(Ruleset ruleset, Direction chooser) {
    return (ruleset instanceof PositiveRuleset);
  }

  public void addRuleset(Ruleset currentGameModeOrStrain) {
    this.currentDeal = new Deal(this.currentBoard, currentGameModeOrStrain);
    this.currentDeal.setCurrentPlayer(this.dealer.next());
    this.currentDeal.setDummy(this.dealer.next(2));
  }

}
