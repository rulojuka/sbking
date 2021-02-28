package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;

public abstract class TrickGame {

	protected Board currentBoard;
	protected Deal currentDeal;
	protected Direction dealer = Direction.NORTH;

	public void dealNewBoard() {
		BoardDealer boardDealer = new BoardDealer();
		this.currentBoard = boardDealer.dealBoard(this.dealer, new ShuffledDeck());
		this.currentDeal = new Deal(currentBoard, new PositiveNoTrumpsRuleset()); // This should be a NoRuleset
	}

	public Direction getDealer() {
		return this.dealer;
	}

	public Board getCurrentBoard() {
		return this.currentBoard;
	}

	public Deal getCurrentDeal() {
		return this.currentDeal;
	}

	public abstract boolean isFinished();

	public abstract void finishDeal();

	public void setPlayerOf(Direction direction, Player player){
		this.currentDeal.setPlayerOf(direction, player);
	}

}
