package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.SIZE_OF_HAND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BoardDealerTest {

	// FIXME This is an integration test as it needs other classes to work.
	@Test
	public void shouldReceiveABoardWithTheCorrectDealerAndACompleteSetOfCards() {
		Direction dealer = Direction.NORTH;
		BoardDealer boardDealer = new ShuffledBoardDealer();
		Board board = boardDealer.dealBoard(dealer);

		// The correct test should verify if new Board(hands, dealer) 
		// was called but Mockito can't do that.
		// Coupling this test with Hand and Board instead :(
		for (Direction direction : Direction.values()) {
			assertEquals(SIZE_OF_HAND, board.getHandOf(direction).size());
		}
		assertEquals(dealer, board.getDealer());

	}

	@Test
	public void shouldReceiveABoardWithTheCorrectDealerAndACompleteSetOfCardsAnd14HCPPlusCardsAndShortestSuit2Plus() {
		Direction dealer = Direction.NORTH;
		BoardDealer boardDealer = new FourteenHCPPlusDoubletonRuledBoardDealer();
		Board board = boardDealer.dealBoard(dealer);

		// Verifying if all hands are complete
		for (Direction direction : Direction.values()) {
			assertEquals(SIZE_OF_HAND, board.getHandOf(direction).size());
		}
		// Verifying if it's the correct dealer
		assertEquals(dealer, board.getDealer());

		// Verifying hands condition: 14 HCP minimum and doubleton ruled
		assertTrue(board.getHandOf(dealer.getPositiveOrNegativeChooserWhenDealer()).getHCP() >= 14);
		assertTrue(board.getHandOf(dealer.getPositiveOrNegativeChooserWhenDealer()).getShortestSuitLength() >= 2);
		
	}

}
