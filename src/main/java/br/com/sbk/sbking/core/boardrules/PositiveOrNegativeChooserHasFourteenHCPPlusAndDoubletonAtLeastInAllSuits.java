package br.com.sbk.sbking.core.boardrules;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits implements BoardRule {

  @Override
  public boolean isValid(Board board) {
    Hand handWithRules = board.getHandOf(board.getDealer().getPositiveOrNegativeChooserWhenDealer());
    HandEvaluations handEvaluations = handWithRules.getHandEvaluations();
    return handEvaluations.getHCP() >= 14 && handEvaluations.getShortestSuitLength() >= 2;
  }

}
