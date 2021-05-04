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

public class DealerHasFourWeakOpeningBoardRuleTest {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasFourWeakOpeningBoardRule subject = new DealerHasFourWeakOpeningBoardRule();

    private boolean hasEightCardInAnySuit = true;
    private boolean doesNotHaveEightCardInAnySuit = false;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dealer = Direction.SOUTH;
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasEightCardInAnySuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasEightOrMoreCardsInAnySuit()).thenReturn(hasEightCardInAnySuit);
    }

    @Test
    public void shouldNotOpenFourWeakWithLessThanSixHCP() {
        int hcp = 5;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenFourWeakWithMoreThanTenHCP() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenFourWeakWithLessThanEightCardInMajorSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, doesNotHaveEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenFourWeakWithCorrectHCPAndDistribution() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertTrue(subject.isValid(board));
    }

}
