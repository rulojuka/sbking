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

public class DealerHasTwoClubsOpeningBoardRuleTest {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasTwoClubsOpeningBoardRule subject = new DealerHasTwoClubsOpeningBoardRule();

    private int twentyTwoHCP = 22;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dealer = Direction.SOUTH;
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
    }

    @Test
    public void shouldNotOpenTwoClubsWithLessThanTwentyTwoHCP() {
        int hcp = 21;
        this.configureParameterizedMocks(hcp);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoClubsWithTwentyTwoHCP() {
        this.configureParameterizedMocks(twentyTwoHCP);

        assertTrue(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoClubsWithMoreThanTwentyTwoHCP() {
        int hcp = 23;
        this.configureParameterizedMocks(hcp);

        assertTrue(subject.isValid(board));
    }

}
