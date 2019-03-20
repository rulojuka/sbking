package br.com.sbk.sbking.core.rulesets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;

public class NegativeWomenRulesetTest {

	private final int NEGATIVE_WOMEN_SCORE_MULTIPLIER = 50;
	private final String NEGATIVE_WOMEN_SHORT_DESCRIPTION = "Negative women";
	private final String NEGATIVE_WOMEN_COMPLETE_DESCRIPTION = "Avoid all queens";

	private NegativeWomenRuleset negativeWomenRuleset;

	@Before
	public void createNegativeWomenRuleset() {
		this.negativeWomenRuleset = new NegativeWomenRuleset();
	}

	@Test
	public void shouldHaveTheCorrectScoreMultiplier() {
		assertEquals(NEGATIVE_WOMEN_SCORE_MULTIPLIER, this.negativeWomenRuleset.getScoreMultiplier());
	}

	@Test
	public void shouldHaveTheCorrectPointsPerTrick() {
		int numberOfWomenInTheTrick = 2;
		Trick trick = mock(Trick.class);
		when(trick.getNumberOfWomen()).thenReturn(numberOfWomenInTheTrick);

		assertEquals(numberOfWomenInTheTrick, this.negativeWomenRuleset.getPoints(trick));
		Mockito.verify(trick, only()).getNumberOfWomen();
	}

	@Test
	public void shouldGetShortDescription() {
		assertEquals(NEGATIVE_WOMEN_SHORT_DESCRIPTION, this.negativeWomenRuleset.getShortDescription());
	}

	@Test
	public void shouldGetCompleteDescription() {
		assertEquals(NEGATIVE_WOMEN_COMPLETE_DESCRIPTION, this.negativeWomenRuleset.getCompleteDescription());
	}

	@Test
	public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
		assertFalse(this.negativeWomenRuleset.prohibitsHeartsUntilOnlySuitLeft());
	}

}
