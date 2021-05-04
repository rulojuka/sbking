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

public class DealerHasOneMajorOpeningBoardRuleTest {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasOneMajorOpeningBoardRule subject = new DealerHasOneMajorOpeningBoardRule();

    private boolean hasFiveCardMajorSuit = true;
    private boolean doesNotHaveFiveCardMajorSuit = false;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dealer = Direction.SOUTH;
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasFiveCardMajorSuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasFiveOrMoreCardsInAMajorSuit()).thenReturn(hasFiveCardMajorSuit);
    }

    @Test
    public void shouldNotOpenOneMajorWithLessThanTwelveHCP() {
        int hcp = 10;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMajorWithMoreThanTwentyOneHCP() {
        int hcp = 22;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMajorWithLessThanFiveCardInMajorSuit() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, doesNotHaveFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenOneMajorWithCorrectHCPAndDistribution() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertTrue(subject.isValid(board));
    }

}
