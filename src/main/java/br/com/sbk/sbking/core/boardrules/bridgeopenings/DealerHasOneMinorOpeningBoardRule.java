package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.boardrules.BoardRule;

public class DealerHasOneMinorOpeningBoardRule implements BoardRule {

    @Override
    public boolean isValid(Board board) {
        Hand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.getHandEvaluations();
        boolean hasElevenPointsOpening = handEvaluations.getHCP() == 11
                && handEvaluations.hasSixOrMoreCardsInAMinorSuit();
        boolean hasNormalOpening = hasCorrectHCPRange(handEvaluations) && hasCorrectDistribution(handEvaluations);
        return hasElevenPointsOpening || hasNormalOpening;
    }

    private boolean hasCorrectHCPRange(HandEvaluations handEvaluations) {
        return handEvaluations.getHCP() >= 12 && handEvaluations.getHCP() <= 21;
    }

    private boolean hasCorrectDistribution(HandEvaluations handEvaluations) {
        return handEvaluations.hasThreeOrMoreCardsInAMinorSuit();
    }

}
