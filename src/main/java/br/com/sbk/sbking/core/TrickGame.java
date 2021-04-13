package br.com.sbk.sbking.core;

public abstract class TrickGame {

    protected Board currentBoard;
    protected Deal currentDeal;
    protected Direction dealer = Direction.NORTH;

    public abstract void dealNewBoard();

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

    public abstract Direction getLeader();

    public void setPlayerOf(Direction direction, Player player) {
        this.currentDeal.setPlayerOf(direction, player);
    }

}
