package br.com.sbk.sbking.core.rulesets.concrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;

public class NegativeKingRulesetTest {

	private static final int NEGATIVE_KING_SCORE_MULTIPLIER = 160;
	private static final String NEGATIVE_KING_SHORT_DESCRIPTION = "Negative king";
	private static final String NEGATIVE_KING_COMPLETE_DESCRIPTION = "Avoid the King of Hearts";

	private NegativeKingRuleset negativeKingRuleset;

	@Before
	public void createNegativeKingRuleset() {
		this.negativeKingRuleset = new NegativeKingRuleset();
	}

	@Test
	public void shouldHaveTheCorrectScoreMultiplier() {
		assertEquals(NEGATIVE_KING_SCORE_MULTIPLIER, this.negativeKingRuleset.getScoreMultiplier());
	}

	@Test
	public void shouldHaveTheCorrectPointsPerTrick() {
		final boolean doesNotHaveKingOfHearts = false;
		final int pointsInATrickWithoutKingOfHearts = 0;
		Trick trickWithoutKingOfHearts = mock(Trick.class);
		when(trickWithoutKingOfHearts.hasKingOfHearts()).thenReturn(doesNotHaveKingOfHearts);

		final boolean hasKingOfHearts = true;
		final int pointsInATrickWithKingOfHearts = 1;
		Trick trickWithKingOfHearts = mock(Trick.class);
		when(trickWithKingOfHearts.hasKingOfHearts()).thenReturn(hasKingOfHearts);

		assertEquals(pointsInATrickWithoutKingOfHearts, this.negativeKingRuleset.getPoints(trickWithoutKingOfHearts));
		assertEquals(pointsInATrickWithKingOfHearts, this.negativeKingRuleset.getPoints(trickWithKingOfHearts));

		Mockito.verify(trickWithoutKingOfHearts, only()).hasKingOfHearts();
		Mockito.verify(trickWithKingOfHearts, only()).hasKingOfHearts();
	}

	@Test
	public void shouldGetShortDescription() {
		assertEquals(NEGATIVE_KING_SHORT_DESCRIPTION, this.negativeKingRuleset.getShortDescription());
	}

	@Test
	public void shouldGetCompleteDescription() {
		assertEquals(NEGATIVE_KING_COMPLETE_DESCRIPTION, this.negativeKingRuleset.getCompleteDescription());
	}

	@Test
	public void shouldProhibitsHeartsUntilOnlySuitLeft() {
		assertTrue(this.negativeKingRuleset.prohibitsHeartsUntilOnlySuitLeft());
	}

}
