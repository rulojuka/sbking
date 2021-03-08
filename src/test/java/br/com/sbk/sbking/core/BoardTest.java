package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BoardTest {

	private Direction dealer;
	private Board board;
	private Map<Direction,Hand> hands = new HashMap<Direction,Hand>();

	@Before
	public void createNorthBoard() {
		dealer = Direction.NORTH;
		for (Direction direction : Direction.values()) {
			hands.put(direction, Mockito.mock(Hand.class));
		}

		board = new Board(hands, dealer);
	}

	@Test
	public void shouldBeConstructedWith4HandsAndADealer() {
		assertNotNull(this.board);
	}

	@Test
	public void shouldSortAllHands() {
		for (Direction direction : Direction.values()) {
			verify(this.hands.get(direction), only()).sort();
		}
	}

	@Test
	public void shouldGetCorrectDealer() {
		assertEquals(this.dealer, this.board.getDealer());
	}

	@Test
	public void shouldGetHandOfAllPossibleDirections() {
		for (Direction direction : Direction.values()) {
			assertEquals(this.hands.get(direction), this.board.getHandOf(direction));
		}
	}

}
