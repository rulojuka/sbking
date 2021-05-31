package br.com.sbk.sbking.core.boardrules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

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
    when(handWithRules.getHandEvaluations()).thenReturn(handEvaluations);
    when(handEvaluations.getHCP()).thenReturn(14);
    when(handEvaluations.getShortestSuitLength()).thenReturn(2);

    Board validBoard = mock(Board.class);
    when(validBoard.getDealer()).thenReturn(dealer);
    when(validBoard.getHandOf(positiveOrNegativeChooser)).thenReturn(handWithRules);

    assertEquals(true, subject.isValid(validBoard));
  }

  @Test
  public void shouldBeInvalidWithValidPointsButInvalidDistribution() {
    Hand handWithRules = mock(Hand.class);
    HandEvaluations handEvaluations = mock(HandEvaluations.class);
    when(handWithRules.getHandEvaluations()).thenReturn(handEvaluations);
    when(handEvaluations.getHCP()).thenReturn(14);
    when(handEvaluations.getShortestSuitLength()).thenReturn(1);

    Board invalidBoard = mock(Board.class);
    when(invalidBoard.getDealer()).thenReturn(dealer);
    when(invalidBoard.getHandOf(positiveOrNegativeChooser)).thenReturn(handWithRules);

    assertEquals(false, subject.isValid(invalidBoard));
  }

  @Test
  public void shouldBeInvalidWithValidDistributionButInvalidPoints() {
    Hand handWithRules = mock(Hand.class);
    HandEvaluations handEvaluations = mock(HandEvaluations.class);
    when(handWithRules.getHandEvaluations()).thenReturn(handEvaluations);
    when(handEvaluations.getHCP()).thenReturn(11);
    when(handEvaluations.getShortestSuitLength()).thenReturn(3);

    Board validBoard = mock(Board.class);
    when(validBoard.getDealer()).thenReturn(dealer);
    when(validBoard.getHandOf(positiveOrNegativeChooser)).thenReturn(handWithRules);

    assertEquals(false, subject.isValid(validBoard));
  }

}
