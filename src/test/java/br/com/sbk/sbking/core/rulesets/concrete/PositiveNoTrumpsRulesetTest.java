package br.com.sbk.sbking.core.rulesets.concrete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;

public class PositiveNoTrumpsRulesetTest {

    private static final int POSITIVE_NO_TRUMPS_SCORE_MULTIPLIER = 25;
    private static final int POSITIVE_POINTS_PER_TRICK = 1;
    private static final String POSITIVE_NO_TRUMPS_SHORT_DESCRIPTION = "NT";
    private static final String POSITIVE_NO_TRUMPS_COMPLETE_DESCRIPTION = "Make the most tricks without a trump suit";
    private PositiveNoTrumpsRuleset positiveNoTrumpsRuleset;

    @BeforeEach
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

    @Test
    public void shouldGetComparator() {
        assertTrue(this.positiveNoTrumpsRuleset.getComparator() instanceof CardInsideHandComparator);
    }

}
