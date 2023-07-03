package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class DealerHasOneNoTrumpOpeningBoardRuleTest {

  @Mock
  private Board board;
  @Mock
  private Hand hand;
  @Mock
  private HandEvaluations handEvaluations;
  private Direction dealer;
  private DealerHasOneNoTrumpOpeningBoardRule subject = new DealerHasOneNoTrumpOpeningBoardRule();

  private boolean balanced = true;
  private boolean unbalanced = false;
  private boolean hasFiveCardMajorSuit = true;
  private boolean doestNotHaveFiveCardMajorSuit = false;
  private int sixteenHCP = 16;
  private int fourteenHCP = 14;
  private int eighteenHCP = 18;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    dealer = Direction.SOUTH;
    when(board.getDealer()).thenReturn(dealer);
    when(board.getHandOf(dealer)).thenReturn(hand);
    when(hand.getHandEvaluations()).thenReturn(handEvaluations);
  }

  private void configureParameterizedMocks(int hcp, boolean isBalanced, boolean hasFiveCardMajorSuit) {
    when(handEvaluations.getHCP()).thenReturn(hcp);
    when(handEvaluations.isBalanced()).thenReturn(isBalanced);
    when(handEvaluations.hasFiveOrMoreCardsInAMajorSuit()).thenReturn(hasFiveCardMajorSuit);
  }

  @Test
  public void shouldNotOpenOneNoTrumpWithFourteenHCP() {
    this.configureParameterizedMocks(fourteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

    assertFalse(subject.isValid(board));
  }

  @Test
  public void shouldNotOpenOneNoTrumpWithEighteenHCP() {
    this.configureParameterizedMocks(eighteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

    assertFalse(subject.isValid(board));
  }

  @Test
  public void shouldNotOpenOneNoTrumpWithUnbalancedHand() {
    this.configureParameterizedMocks(sixteenHCP, unbalanced, doestNotHaveFiveCardMajorSuit);

    assertFalse(subject.isValid(board));
  }

  @Test
  public void shouldNotOpenOneNoTrumpWithFiveCardMajor() {
    this.configureParameterizedMocks(sixteenHCP, balanced, hasFiveCardMajorSuit);

    assertFalse(subject.isValid(board));
  }

  @Test
  public void shouldOpenOneNoTrumpWithCorrectHCPAndDistribution() {
    this.configureParameterizedMocks(sixteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

    assertTrue(subject.isValid(board));
  }

}
