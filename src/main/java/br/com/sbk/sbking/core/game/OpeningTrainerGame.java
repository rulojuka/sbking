package br.com.sbk.sbking.core.game;

import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.BoardDealer;
import br.com.sbk.sbking.core.boardrules.BridgeOpener;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;

public class OpeningTrainerGame extends TrickGame {

  private BoardDealer boardDealer;
  private BridgeContract opening;
  private BridgeOpener opener;

  public OpeningTrainerGame(BoardDealer boardDealer, BridgeOpener bridgeOpener) {
    this.boardDealer = boardDealer;
    this.opener = bridgeOpener;
    this.dealer = Direction.NORTH;
  }

  @Override
  public void dealNewBoard() {
    this.currentBoard = this.boardDealer.dealBoard(this.dealer);
    this.currentDeal = new Deal(currentBoard, new NoRuleset(), this.getLeader());
    this.opening = this.opener.getOpening(this.currentBoard.getHandOf(this.dealer));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void finishDeal() {
  }

  @Override
  public Direction getLeader() {
    return this.dealer.next();
  }

  public BridgeContract getOpening() {
    return opening;
  }

}
