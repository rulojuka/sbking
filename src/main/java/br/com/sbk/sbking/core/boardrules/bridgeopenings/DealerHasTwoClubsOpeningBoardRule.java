package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BoardRule;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class DealerHasTwoClubsOpeningBoardRule implements BoardRule {

    @Override
    public boolean isValid(Board board) {
        Hand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.getHandEvaluations();
        return handEvaluations.getHCP() >= 22;
    }

}
