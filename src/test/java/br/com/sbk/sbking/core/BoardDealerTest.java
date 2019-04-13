package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardDealerTest {

	private static final int SIZE_OF_HAND = 13;

	// FIXME This is an integration test as it needs other classes to work.
	@Test
	public void shouldReceiveABoardWithTheCorrectDealerAndACompleteSetOfCards() {
		Direction dealer = Direction.NORTH;

		Board board = BoardDealer.dealBoard(dealer);

		// The correct test should verify if new Board(completeHand1, completeHand2,
		// completeHand3, completeHand4, dealer) was called but Mockito can't do that.
		// Coupling this test with Hand and Board instead :(
		for (Direction direction : Direction.values()) {
			assertEquals(SIZE_OF_HAND, board.getHandOf(direction).size());
		}
		assertEquals(dealer, board.getDealer());

	}

}
