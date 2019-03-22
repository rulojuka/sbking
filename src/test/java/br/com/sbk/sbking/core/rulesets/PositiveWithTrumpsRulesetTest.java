package br.com.sbk.sbking.core.rulesets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public class PositiveWithTrumpsRulesetTest {

	private static final int POSITIVE_WITH_TRUMPS_SCORE_MULTIPLIER = 25;
	private static final int POSITIVE_POINTS_PER_TRICK = 1;
	private static final String POSITIVE_SPADES_SHORT_DESCRIPTION = "Positive Spades";
	private static final String POSITIVE_SPADES_COMPLETE_DESCRIPTION = "Make the most tricks with Spades as trump suit";
	private PositiveWithTrumpsRuleset positiveWithTrumpRuleset;

	@Before
	public void createPositiveNoTrumpsRuleset() {
		Suit spades = Suit.SPADES;
		this.positiveWithTrumpRuleset = new PositiveWithTrumpsRuleset(spades);
	}

	@Test
	public void shouldHaveTheCorrectScoreMultiplier() {
		assertEquals(POSITIVE_WITH_TRUMPS_SCORE_MULTIPLIER, this.positiveWithTrumpRuleset.getScoreMultiplier());
	}

	@Test
	public void shouldHaveTheCorrectPointsPerTrick() {
		Trick anyTrick = mock(Trick.class);
		assertEquals(POSITIVE_POINTS_PER_TRICK, this.positiveWithTrumpRuleset.getPoints(anyTrick));
		verifyZeroInteractions(anyTrick);
	}

	@Test
	public void shouldGetShortDescription() {
		assertEquals(POSITIVE_SPADES_SHORT_DESCRIPTION, this.positiveWithTrumpRuleset.getShortDescription());
	}

	@Test
	public void shouldGetCompleteDescription() {
		assertEquals(POSITIVE_SPADES_COMPLETE_DESCRIPTION, this.positiveWithTrumpRuleset.getCompleteDescription());
	}

	@Test
	public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
		assertFalse(this.positiveWithTrumpRuleset.prohibitsHeartsUntilOnlySuitLeft());
	}

}
