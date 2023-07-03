package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;

public class DealerHasThreeWeakOpeningBoardRuleTest {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasThreeWeakOpeningBoardRule subject = new DealerHasThreeWeakOpeningBoardRule();

    private boolean hasSevenCardInAnySuit = true;
    private boolean doesNotHaveSevenCardInAnySuit = false;
    private boolean hasThreeOutOfFiveHigherCards = true;
    private boolean doesNotHaveThreeOutOfFiveHigherCards = false;
    private boolean hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards = true;
    private boolean doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards = false;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dealer = Direction.SOUTH;
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasSevenCardsInLongestSuit,
            boolean hasThreeOutOfFiveHigherCards, boolean hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasSevenCardsInLongestSuit()).thenReturn(hasSevenCardsInLongestSuit);
        when(handEvaluations.hasThreeOutOfFiveHigherCards(any())).thenReturn(hasThreeOutOfFiveHigherCards);
        when(handEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit())
                .thenReturn(hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);
    }

    @Test
    public void shouldNotOpenThreeWeakWithLessThanSixHCP() {
        int hcp = 5;
        this.configureParameterizedMocks(hcp, hasSevenCardInAnySuit, hasThreeOutOfFiveHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenThreeWeakWithMoreThanTenHCP() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasSevenCardInAnySuit, hasThreeOutOfFiveHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenThreeWeakWithFourCardsInTheMajorSuitExcludingLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSevenCardInAnySuit, hasThreeOutOfFiveHigherCards,
                hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenThreeWeakWithoutThreeHighCardsInLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSevenCardInAnySuit, doesNotHaveThreeOutOfFiveHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenThreeWeakWithoutSevenCardsInTheLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, doesNotHaveSevenCardInAnySuit, hasThreeOutOfFiveHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenThreeWeakWithCorrectHCPAndDistribution() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSevenCardInAnySuit, hasThreeOutOfFiveHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertTrue(subject.isValid(board));
    }

}
