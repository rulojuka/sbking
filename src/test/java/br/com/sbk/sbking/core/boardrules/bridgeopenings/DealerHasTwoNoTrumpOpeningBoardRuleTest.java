package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class DealerHasTwoNoTrumpOpeningBoardRuleTest {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasTwoNoTrumpOpeningBoardRule subject = new DealerHasTwoNoTrumpOpeningBoardRule();

    private boolean balanced = true;
    private boolean unbalanced = false;
    private int twentyHCP = 20;
    private int twentyOneHCP = 21;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dealer = Direction.SOUTH;
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean isBalanced) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.isBalanced()).thenReturn(isBalanced);
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithLessThanTwentyHCP() {
        int hcp = 19;
        this.configureParameterizedMocks(hcp, balanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithMoreThanTwentyOneHCP() {
        int hcp = 23;
        this.configureParameterizedMocks(hcp, balanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithUnbalancedHand() {
        this.configureParameterizedMocks(twentyHCP, unbalanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoNoTrumpWithTwentyHCPAndBalancedDistribution() {
        this.configureParameterizedMocks(twentyHCP, balanced);

        assertTrue(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoNoTrumpWithTwentyOneHCPAndBalancedDistribution() {
        this.configureParameterizedMocks(twentyOneHCP, balanced);

        assertTrue(subject.isValid(board));
    }

}
