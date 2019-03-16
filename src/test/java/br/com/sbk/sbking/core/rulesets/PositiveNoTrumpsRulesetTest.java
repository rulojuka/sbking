package br.com.sbk.sbking.core.rulesets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.Trick;

public class PositiveNoTrumpsRulesetTest {

	private final int POSITIVE_NO_TRUMPS_SCORE_MULTIPLIER = 25;
	private final int POSITIVE_POINTS_PER_TRICK = 1;
	private final String POSITIVE_NO_TRUMPS_SHORT_DESCRIPTION = "Positive no trumps";
	private final String POSITIVE_NO_TRUMPS_COMPLETE_DESCRIPTION = "Make the most tricks without a trump suit";
	private PositiveNoTrumpsRuleset positiveNoTrumpsRuleset;

	@Before
	public void createPositiveNoTrumpsRuleset() {
		this.positiveNoTrumpsRuleset = new PositiveNoTrumpsRuleset();
	}

	@Test
	public void shouldHaveTheCorrectScoreMultiplier() {
		assertEquals(POSITIVE_NO_TRUMPS_SCORE_MULTIPLIER, this.positiveNoTrumpsRuleset.getScoreMultiplier());
	}

	@Test
	public void shouldHaveTheCorrectPointsPerTrick() {
		Trick anyTrick = mock(Trick.class);
		assertEquals(POSITIVE_POINTS_PER_TRICK, this.positiveNoTrumpsRuleset.getPoints(anyTrick));
		verifyZeroInteractions(anyTrick);
	}

	@Test
	public void shouldGetShortDescription() {
		assertEquals(POSITIVE_NO_TRUMPS_SHORT_DESCRIPTION, this.positiveNoTrumpsRuleset.getShortDescription());
	}

	@Test
	public void shouldGetCompleteDescription() {
		assertEquals(POSITIVE_NO_TRUMPS_COMPLETE_DESCRIPTION, this.positiveNoTrumpsRuleset.getCompleteDescription());
	}

	@Test
	public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
		assertFalse(this.positiveNoTrumpsRuleset.prohibitsHeartsUntilOnlySuitLeft());
	}

}
