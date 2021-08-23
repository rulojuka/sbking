package br.com.sbk.sbking.core.game;

import java.util.Deque;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;

public abstract class TrickGame {

    protected Board currentBoard;
    protected Deal currentDeal;
    protected Direction dealer = Direction.NORTH;
    protected Deque<Card> gameDeck;

    protected TrickGame(Deque<Card> gameDeck) {
        super();
        this.gameDeck = gameDeck;
    }

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

    public void setRuleset(Ruleset ruleset) {
        this.currentDeal.setRuleset(ruleset);
    }

}
