package br.com.sbk.sbking.core.rulesets.concrete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;

public class NegativeTricksRulesetTest {

    private static final int NEGATIVE_TRICKS_SCORE_MULTIPLIER = 20;
    private static final int NEGATIVE_POINTS_PER_TRICK = 1;
    private static final String NEGATIVE_TRICKS_SHORT_DESCRIPTION = "Negative tricks";
    private static final String NEGATIVE_TRICKS_COMPLETE_DESCRIPTION = "Avoid all tricks";
    private static final int TOTAL_NUMBER_OF_TRICKS = 13;
    private NegativeTricksRuleset negativeTricksRuleset;

    @BeforeEach
    public void createNegativeTricksRuleset() {
        this.negativeTricksRuleset = new NegativeTricksRuleset();
    }

    @Test
    public void shouldHaveTheCorrectScoreMultiplier() {
        assertEquals(NEGATIVE_TRICKS_SCORE_MULTIPLIER, this.negativeTricksRuleset.getScoreMultiplier());
    }

    @Test
    public void shouldHaveTheCorrectPointsPerTrick() {
        Trick anyTrick = mock(Trick.class);
        assertEquals(NEGATIVE_POINTS_PER_TRICK, this.negativeTricksRuleset.getPoints(anyTrick));
        verifyNoInteractions(anyTrick);
    }

    @Test
    public void shouldGetShortDescription() {
        assertEquals(NEGATIVE_TRICKS_SHORT_DESCRIPTION, this.negativeTricksRuleset.getShortDescription());
    }

    @Test
    public void shouldGetCompleteDescription() {
        assertEquals(NEGATIVE_TRICKS_COMPLETE_DESCRIPTION, this.negativeTricksRuleset.getCompleteDescription());
    }

    @Test
    public void shouldNotProhibitsHeartsUntilOnlySuitLeft() {
        assertFalse(this.negativeTricksRuleset.prohibitsHeartsUntilOnlySuitLeft());
    }

    @Test
    public void shouldGetTotalPoints() {
        assertEquals(TOTAL_NUMBER_OF_TRICKS, this.negativeTricksRuleset.getTotalPoints());
    }

    @Test
    public void shouldGetComparator() {
        assertTrue(this.negativeTricksRuleset.getComparator() instanceof CardInsideHandComparator);
    }

}
