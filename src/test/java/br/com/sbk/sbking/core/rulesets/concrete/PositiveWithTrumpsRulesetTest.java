package br.com.sbk.sbking.core.rulesets.concrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.cardComparators.CardInsideHandWithSuitComparator;

public class PositiveWithTrumpsRulesetTest {

    private static final int POSITIVE_WITH_TRUMPS_SCORE_MULTIPLIER = 25;
    private static final int POSITIVE_POINTS_PER_TRICK = 1;
    private static final String POSITIVE_SPADES_SHORT_DESCRIPTION = "Positive spades";
    private static final String POSITIVE_SPADES_COMPLETE_DESCRIPTION = "Make the most tricks with spades as trump suit";
    private PositiveWithTrumpsRuleset positiveWithTrumpRuleset;
    private Suit trumpSuit;

    @Before
    public void createPositiveNoTrumpsRuleset() {
        trumpSuit = Suit.SPADES;
        this.positiveWithTrumpRuleset = new PositiveWithTrumpsRuleset(trumpSuit);
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

    @Test
    public void shouldGetTrumpSuit() {
        assertEquals(trumpSuit, this.positiveWithTrumpRuleset.getTrumpSuit());
    }

    @Test
    public void shouldGetComparator() {
        assertEquals(trumpSuit, this.positiveWithTrumpRuleset.getTrumpSuit());
        assertTrue(this.positiveWithTrumpRuleset.getComparator() instanceof CardInsideHandWithSuitComparator);
    }

}
