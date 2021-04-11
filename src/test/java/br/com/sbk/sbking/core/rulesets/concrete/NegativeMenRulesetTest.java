package br.com.sbk.sbking.core.rulesets.concrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.cardComparators.CardInsideHandComparator;

public class NegativeMenRulesetTest {

    private static final int NEGATIVE_MEN_SCORE_MULTIPLIER = 30;
    private static final String NEGATIVE_MEN_SHORT_DESCRIPTION = "Negative men";
    private static final String NEGATIVE_MEN_COMPLETE_DESCRIPTION = "Avoid all jacks and kings";
    private static final Object TOTAL_NUMBER_OF_MEN = 8;

    private NegativeMenRuleset negativeMenRuleset;

    @Before
    public void createNegativeMenRuleset() {
        this.negativeMenRuleset = new NegativeMenRuleset();
    }

    @Test
    public void shouldHaveTheCorrectScoreMultiplier() {
        assertEquals(NEGATIVE_MEN_SCORE_MULTIPLIER, this.negativeMenRuleset.getScoreMultiplier());
    }

    @Test
    public void shouldHaveTheCorrectPointsPerTrick() {
        int numberOfMenInTheTrick = 3;
        Trick trick = mock(Trick.class);
        when(trick.getNumberOfMen()).thenReturn(numberOfMenInTheTrick);

        assertEquals(numberOfMenInTheTrick, this.negativeMenRuleset.getPoints(trick));
        Mockito.verify(trick, only()).getNumberOfMen();
    }

    @Test
    public void shouldGetShortDescription() {
        assertEquals(NEGATIVE_MEN_SHORT_DESCRIPTION, this.negativeMenRuleset.getShortDescription());
    }

    @Test
    public void shouldGetCompleteDescription() {
        assertEquals(NEGATIVE_MEN_COMPLETE_DESCRIPTION, this.negativeMenRuleset.getCompleteDescription());
    }

    @Test
    public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
        assertFalse(this.negativeMenRuleset.prohibitsHeartsUntilOnlySuitLeft());
    }

    @Test
    public void shouldGetTotalPoints() {
        assertEquals(TOTAL_NUMBER_OF_MEN, this.negativeMenRuleset.getTotalPoints());
    }

    @Test
    public void shouldGetComparator() {
        assertTrue(this.negativeMenRuleset.getComparator() instanceof CardInsideHandComparator);
    }

}
