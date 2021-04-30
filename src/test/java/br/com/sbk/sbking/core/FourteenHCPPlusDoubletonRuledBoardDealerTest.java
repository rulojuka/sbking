package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.SIZE_OF_HAND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FourteenHCPPlusDoubletonRuledBoardDealerTest {

    // FIXME This is an integration test as it needs other classes to work.
    @Test
    public void shouldReceiveABoardWithTheCorrectDealerAndACompleteSetOfCardsAnd14HCPPlusCardsAndShortestSuit2Plus() {
        Direction dealer = Direction.NORTH;
        BoardDealer boardDealer = new FourteenHCPPlusDoubletonRuledBoardDealer();
        Board board = boardDealer.dealBoard(dealer);

        for (Direction direction : Direction.values()) {
            assertEquals(SIZE_OF_HAND, board.getHandOf(direction).size());
        }

        assertEquals(dealer, board.getDealer());

        this.validateHand(board.getHandOf(dealer.getPositiveOrNegativeChooserWhenDealer()));

    }

    private void validateHand(Hand hand) {
        HandEvaluations handEvaluations = hand.getHandEvaluations();
        assertTrue(handEvaluations.getHCP() >= 14);
        assertTrue(handEvaluations.getShortestSuitLength() >= 2);
    }

}
