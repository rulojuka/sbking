package br.com.sbk.sbking.core.boardrules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuitsTest {

  private Direction dealer = Direction.SOUTH;
  private Direction positiveOrNegativeChooser = dealer.getPositiveOrNegativeChooserWhenDealer();
  private BoardRule subject = new PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits();

  @Test
  public void shouldBeValidWithAValidBoard() {
    Hand handWithRules = mock(Hand.class);
    HandEvaluations handEvaluations = mock(HandEvaluations.class);

    Board validBoard = mock(Board.class);
    setupBoardGetDealerAndGetHandEvaluationsAndGetHCPAnd2MoreStubs(handWithRules, handEvaluations, validBoard, 14, 2);

    assertEquals(true, subject.isValid(validBoard));
  }

  @Test
  public void shouldBeInvalidWithValidPointsButInvalidDistribution() {
    Hand handWithRules = mock(Hand.class);
    HandEvaluations handEvaluations = mock(HandEvaluations.class);

    Board invalidBoard = mock(Board.class);
    setupBoardGetDealerAndGetHandEvaluationsAndGetHCPAnd2MoreStubs(handWithRules, handEvaluations, invalidBoard, 14, 1);

    assertEquals(false, subject.isValid(invalidBoard));
  }

  @Test
  public void shouldBeInvalidWithValidDistributionButInvalidPoints() {
    Hand handWithRules = mock(Hand.class);
    HandEvaluations handEvaluations = mock(HandEvaluations.class);

    Board validBoard = mock(Board.class);
    setupBoardGetDealerAndGetHandEvaluationsAndGetHCPAnd2MoreStubs(handWithRules, handEvaluations, validBoard, 11, 3);

    assertEquals(false, subject.isValid(validBoard));
  }

  private void setupBoardGetDealerAndGetHandEvaluationsAndGetHCPAnd2MoreStubs(Hand handWithRules, HandEvaluations handEvaluations, Board validBoard, int getHCPReturnValue, int getShortestSuitLengthReturnValue) {
    when(handWithRules.getHandEvaluations()).thenReturn(handEvaluations);
    when(handEvaluations.getHCP()).thenReturn(getHCPReturnValue);
    when(handEvaluations.getShortestSuitLength()).thenReturn(getShortestSuitLengthReturnValue);
    when(validBoard.getDealer()).thenReturn(dealer);
    when(validBoard.getHandOf(positiveOrNegativeChooser)).thenReturn(handWithRules);
  }
}
