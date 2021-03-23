package br.com.sbk.sbking.core;

import java.util.List;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public class CagandoNoBequinhoGame extends TrickGame {

    private int currentNumberOfCardsInAHand;
    private Card trumpCard;

    public CagandoNoBequinhoGame() {
        super();
        this.currentNumberOfCardsInAHand = 13;
        this.dealNewBoard();
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
        int numberOfRemovedCards = 13 - this.currentNumberOfCardsInAHand;
        List<Card> removedCards = null;
        for (int i = 0; i < numberOfRemovedCards; i++) {
            removedCards = this.currentBoard.removeOneCardFromEachHand();
        }

        if (removedCards == null || removedCards.isEmpty()) {
            this.trumpCard = null;
            this.addRuleset(new PositiveNoTrumpsRuleset());
        } else {
            this.trumpCard = removedCards.get(0);
            this.addRuleset(new PositiveWithTrumpsRuleset(this.trumpCard.getSuit()));
        }

        this.currentDeal.setStartingNumberOfCardsInTheHand(this.currentNumberOfCardsInAHand);

    }

    private void addRuleset(Ruleset positiveRuleset) {
        this.currentDeal = new Deal(this.currentBoard, positiveRuleset);
    }

}
