package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.boardrules.BoardRule;

public class DealerHasThreeWeakOpeningBoardRule implements BoardRule {

    @Override
    public boolean isValid(Board board) {
        Hand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.getHandEvaluations();
        return hasCorrectHCPRange(handEvaluations) && hasCorrectDistribution(handEvaluations);
    }

    private boolean hasCorrectHCPRange(HandEvaluations handEvaluations) {
        return handEvaluations.getHCP() >= 6 && handEvaluations.getHCP() <= 10;
    }

    private boolean hasCorrectDistribution(HandEvaluations handEvaluations) {
        Suit suit = handEvaluations.getLongestSuit();
        return handEvaluations.hasSevenCardsInLongestSuit()
                && !handEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit()
                && handEvaluations.hasThreeOutOfFiveHigherCards(suit);
    }

}
