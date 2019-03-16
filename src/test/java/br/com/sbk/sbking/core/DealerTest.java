package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.rulesets.Ruleset;

public class DealerTest {
	
	private Dealer dealer;
	private final int SIZE_OF_HAND = 13;
	
	@Before
	public void createDealer() {
		dealer = new Dealer();
	}

	@Test
	public void shouldCallBoardConstructorWith4HandsAndTheGivenRuleset() {
		Ruleset ruleset = mock(Ruleset.class);
		
		Board dealedBoard = dealer.deal(ruleset);
		
		// The correct test should verify if new Board(anyListOfHandsWith13Cards, ruleset) was called
		// but Mockito can't do that. Coupling this test with Board instead :(
		assertEquals(SIZE_OF_HAND,dealedBoard.getNorthHand().getListOfCards().size());
		assertEquals(SIZE_OF_HAND,dealedBoard.getEastHand().getListOfCards().size());
		assertEquals(SIZE_OF_HAND,dealedBoard.getSouthHand().getListOfCards().size());
		assertEquals(SIZE_OF_HAND,dealedBoard.getWestHand().getListOfCards().size());
		assertEquals(ruleset,dealedBoard.getRuleset());
		
	}

}
