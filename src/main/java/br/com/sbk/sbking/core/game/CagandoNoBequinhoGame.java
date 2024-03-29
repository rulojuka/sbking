package br.com.sbk.sbking.core.game;

import java.util.Deque;
import java.util.List;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.BoardDealer;
import br.com.sbk.sbking.core.boarddealer.ShuffledBoardDealer;
import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public class CagandoNoBequinhoGame extends TrickGame {

    private int currentNumberOfCardsInAHand;
    private Card trumpCard;
    private BoardDealer boardDealer;

    public CagandoNoBequinhoGame(Deque<Card> gameDeck) {
        super(gameDeck);
        this.currentNumberOfCardsInAHand = 2;
        this.boardDealer = new ShuffledBoardDealer();
    }

    @Override
    public boolean isFinished() {
        return currentNumberOfCardsInAHand > 13;
    }

    @Override
    public void finishDeal() {
        this.dealer = this.dealer.next();
        this.currentNumberOfCardsInAHand++;
    }

    public int getCurrentNumberOfCardsInAHand() {
        return currentNumberOfCardsInAHand;
    }

    @Override
    public void dealNewBoard() {

        this.currentBoard = this.boardDealer.dealBoard(this.dealer, gameDeck);
        int numberOfRemovedCards = 13 - this.currentNumberOfCardsInAHand;
        List<Card> removedCards = null;
        for (int i = 0; i < numberOfRemovedCards; i++) {
            removedCards = this.currentBoard.removeOneCardFromEachHand();
        }
        this.currentBoard.sortAllHands(new CardInsideHandComparator());

        if (removedCards == null || removedCards.isEmpty()) {
            this.trumpCard = null;
            this.createDeal(new PositiveNoTrumpsRuleset());
        } else {
            this.trumpCard = removedCards.get(0);
            this.createDeal(new PositiveWithTrumpsRuleset(this.trumpCard.getSuit()));
        }

    }

    private void createDeal(Ruleset positiveRuleset) {
        this.currentDeal = new Deal(this.currentBoard, positiveRuleset, this.getLeader(), false);
    }

    @Override
    public Direction getLeader() {
        return this.dealer;
    }

}
