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
import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;

public class NegativeLastTwoRulesetTest {

    private static final int NEGATIVE_LAST_TWO_SCORE_MULTIPLIER = 90;
    private static final String NEGATIVE_LAST_TWO_SHORT_DESCRIPTION = "Negative last two";
    private static final String NEGATIVE_LAST_TWO_COMPLETE_DESCRIPTION = "Avoid the last two tricks";
    private static final int TOTAL_NUMBER_OF_LAST_TWO_TRICKS = 2;

    private NegativeLastTwoRuleset negativeLastTwoRuleset;

    @Before
    public void createNegativeLastTwoRuleset() {
        this.negativeLastTwoRuleset = new NegativeLastTwoRuleset();
    }

    @Test
    public void shouldHaveTheCorrectScoreMultiplier() {
        assertEquals(NEGATIVE_LAST_TWO_SCORE_MULTIPLIER, this.negativeLastTwoRuleset.getScoreMultiplier());
    }

    @Test
    public void shouldHaveTheCorrectPointsPerTrick() {
        boolean notOneOfTheLastTwo = false;
        int pointsInANotLastTwoTrick = 0;
        Trick trickFive = mock(Trick.class);
        when(trickFive.isLastTwo()).thenReturn(notOneOfTheLastTwo);

        boolean oneOfTheLastTwo = true;
        int pointsInALastTwoTrick = 1;
        Trick trickTwelve = mock(Trick.class);
        when(trickTwelve.isLastTwo()).thenReturn(oneOfTheLastTwo);

        assertEquals(pointsInANotLastTwoTrick, this.negativeLastTwoRuleset.getPoints(trickFive));
        assertEquals(pointsInALastTwoTrick, this.negativeLastTwoRuleset.getPoints(trickTwelve));

        Mockito.verify(trickFive, only()).isLastTwo();
        Mockito.verify(trickTwelve, only()).isLastTwo();
    }

    @Test
    public void shouldGetShortDescription() {
        assertEquals(NEGATIVE_LAST_TWO_SHORT_DESCRIPTION, this.negativeLastTwoRuleset.getShortDescription());
    }

    @Test
    public void shouldGetCompleteDescription() {
        assertEquals(NEGATIVE_LAST_TWO_COMPLETE_DESCRIPTION, this.negativeLastTwoRuleset.getCompleteDescription());
    }

    @Test
    public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
        assertFalse(this.negativeLastTwoRuleset.prohibitsHeartsUntilOnlySuitLeft());
    }

    @Test
    public void shouldGetTotalPoints() {
        assertEquals(TOTAL_NUMBER_OF_LAST_TWO_TRICKS, this.negativeLastTwoRuleset.getTotalPoints());
    }

    @Test
    public void shouldGetComparator() {
        assertTrue(this.negativeLastTwoRuleset.getComparator() instanceof CardInsideHandComparator);
    }

}
