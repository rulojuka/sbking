package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import br.com.sbk.sbking.core.rulesets.Ruleset;

public class DealTest {

	private final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	@Test
	public void shouldConstructADealWithTheLeaderFromTheBoard() {
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Direction leader = Direction.NORTH;
		when(board.getLeader()).thenReturn(leader);
		Deal deal = new Deal(board, ruleset);

		assertEquals(leader, deal.getCurrentPlayer());
	}

	@Test
	public void shouldConstructADealWithTheGivenRuleset() {
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Deal deal = new Deal(board, ruleset);

		assertEquals(ruleset, deal.getRuleset());
	}

	@Test
	public void shouldConstructADealWithNoPoints() {
		int noPoints = 0;
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Deal deal = new Deal(board, ruleset);
		assertEquals(noPoints, deal.getNorthSouthPoints());
		assertEquals(noPoints, deal.getEastWestPoints());
	}

	@Test
	public void shouldConstructADealWithACurrentTrickWithTheCorrectLeader() {
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Direction leader = Direction.NORTH;
		when(board.getLeader()).thenReturn(leader);
		Deal deal = new Deal(board, ruleset);
		// The correct test would be verifying if new Trick(leader) was called
		// but Mockito doesn't do that. So, coupling the test with Trick :(
		assertEquals(leader, deal.getCurrentTrick().getLeader());
	}

	@Test
	public void shouldConstructADealWithNoCompletedTricks() {
		fail("Not implemented yet.");
	}

	@Test
	public void shouldGetHandOfACertainDirection() {
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Direction west = Direction.WEST;
		Hand westHand = mock(Hand.class);
		when(board.getHandOf(west)).thenReturn(westHand);
		Deal deal = new Deal(board, ruleset);

		assertEquals(westHand, deal.getHandOf(west));
	}

	@Test
	public void shouldGetCurrentTrick() {
		Board board = mock(Board.class);
		Ruleset ruleset = mock(Ruleset.class);
		Deal deal = new Deal(board, ruleset);

		deal.getCurrentTrick();
	}

	@Test
	public void shouldPlayCard() {
		fail("Not implemented yet.");
	}

}
