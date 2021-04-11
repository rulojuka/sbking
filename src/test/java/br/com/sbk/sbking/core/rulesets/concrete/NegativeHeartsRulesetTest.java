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
import br.com.sbk.sbking.core.cardComparators.CardInsideHandComparator;

public class NegativeHeartsRulesetTest {

    private static final int NEGATIVE_HEARTS_SCORE_MULTIPLIER = 20;
    private static final String NEGATIVE_HEARTS_SHORT_DESCRIPTION = "Negative hearts";
    private static final String NEGATIVE_HEARTS_COMPLETE_DESCRIPTION = "Avoid all hearts cards";
    private static final int TOTAL_NUMBER_OF_HEARTS_CARDS = 13;

    NegativeHeartsRuleset negativeHeartsRuleset;

    @Before
    public void createNegativeHeartsRuleset() {
        this.negativeHeartsRuleset = new NegativeHeartsRuleset();
    }

    @Test
    public void shouldHaveTheCorrectScoreMultiplier() {
        assertEquals(NEGATIVE_HEARTS_SCORE_MULTIPLIER, this.negativeHeartsRuleset.getScoreMultiplier());
    }

    @Test
    public void shouldHaveTheCorrectPointsPerTrick() {
        int numberOfHeartsCardsInTheTrick = 3;
        Trick trick = mock(Trick.class);
        when(trick.getNumberOfHeartsCards()).thenReturn(numberOfHeartsCardsInTheTrick);

        assertEquals(numberOfHeartsCardsInTheTrick, this.negativeHeartsRuleset.getPoints(trick));
        Mockito.verify(trick, only()).getNumberOfHeartsCards();
    }

    @Test
    public void shouldGetShortDescription() {
        assertEquals(NEGATIVE_HEARTS_SHORT_DESCRIPTION, this.negativeHeartsRuleset.getShortDescription());
    }

    @Test
    public void shouldGetCompleteDescription() {
        assertEquals(NEGATIVE_HEARTS_COMPLETE_DESCRIPTION, this.negativeHeartsRuleset.getCompleteDescription());
    }

    @Test
    public void shouldProhibitsHeartsUntilOnlySuitLeft() {
        assertTrue(this.negativeHeartsRuleset.prohibitsHeartsUntilOnlySuitLeft());

    }

    @Test
    public void shouldGetTotalPoints() {
        assertEquals(TOTAL_NUMBER_OF_HEARTS_CARDS, this.negativeHeartsRuleset.getTotalPoints());
    }

    @Test
    public void shouldGetComparator() {
        assertTrue(this.negativeHeartsRuleset.getComparator() instanceof CardInsideHandComparator);
    }

}
