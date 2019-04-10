package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BoardTest {

	private Direction dealer;
	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Board board;

	@Before
	public void createNorthBoard() {
		dealer = Direction.NORTH;
		northHand = Mockito.mock(Hand.class);
		eastHand = Mockito.mock(Hand.class);
		southHand = Mockito.mock(Hand.class);
		westHand = Mockito.mock(Hand.class);

		board = new Board(northHand, eastHand, southHand, westHand, dealer);
	}

	@Test
	public void shouldBeConstructedWith4HandsAndADealer() {
		assertNotNull(this.board);
	}

	@Test
	public void shouldSortAllHands() {
		verify(this.northHand, only()).sort();
		verify(this.eastHand, only()).sort();
		verify(this.southHand, only()).sort();
		verify(this.westHand, only()).sort();
	}

	@Test
	public void shouldGetCorrectDealer() {
		assertEquals(this.dealer, this.board.getDealer());
	}

	@Test
	public void shouldGetHandOfACertainDirection() {
		assertEquals(this.westHand, this.board.getHandOf(Direction.WEST));
	}

}
