package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import br.com.sbk.sbking.core.rulesets.Ruleset;

public class DealerTest {

	private final int SIZE_OF_HAND = 13;

	@Test
	public void shouldCallDealConstructorWithABoardWithTheCorrectLeaderAndTheGivenRuleset() {
		Direction leader = Direction.NORTH;
		Dealer dealer = new Dealer(leader);
		Ruleset ruleset = mock(Ruleset.class);

		Deal deal = dealer.deal(ruleset);

		// The correct test should verify if new Deal(anyBoardWithSameDealer, ruleset)
		// was called
		// but Mockito can't do that. Coupling this test with Deal instead :(
		for (Direction direction : Direction.values()) {
			assertEquals(SIZE_OF_HAND, deal.getHandOf(direction).getListOfCards().size());
		}
		assertEquals(ruleset, deal.getRuleset());

	}

}
