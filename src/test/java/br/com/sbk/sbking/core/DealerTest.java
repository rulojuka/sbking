package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class DealerTest {

	private static final int SIZE_OF_HAND = 13;

	@Test
	public void shouldCallDealConstructorWithABoardWithTheCorrectDealerAndTheGivenRuleset() {
		Direction dealer = Direction.NORTH;
		CompleteDealDealer boardDealer = new CompleteDealDealer(dealer);
		Ruleset ruleset = mock(Ruleset.class);

		Deal deal = boardDealer.deal(ruleset);

		// The correct test should verify if new Deal(anyBoardWithSameDealer, ruleset)
		// was called
		// but Mockito can't do that. Coupling this test with Deal instead :(
		for (Direction direction : Direction.values()) {
			assertEquals(SIZE_OF_HAND, deal.getHandOf(direction).size());
		}
		assertEquals(ruleset, deal.getRuleset());

	}

}
