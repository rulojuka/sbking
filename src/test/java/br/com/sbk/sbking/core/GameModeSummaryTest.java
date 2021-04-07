package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class GameModeSummaryTest {

    private Direction north = Direction.NORTH;
    private Ruleset ruleset;
    private Integer orderOfPlay;
    private String orderOfPlayString;
    private Score score;
    private GameModeSummary northChosenGameModeSummary;

    @Before
    public void setup() {
        this.ruleset = Mockito.mock(Ruleset.class);
        this.orderOfPlay = Integer.valueOf(10);
        this.orderOfPlayString = "10";
        this.score = Mockito.mock(Score.class);
        northChosenGameModeSummary = new GameModeSummary(north, ruleset, orderOfPlay, score);
    }

    @Test
    public void shouldGetNameFromRulesetShortDescription() {
        northChosenGameModeSummary.getName();

        Mockito.verify(ruleset).getShortDescription();
    }

    @Test
    public void shouldGetScoreFromScoreFinalPunctuation() {
        northChosenGameModeSummary.getScore();

        Mockito.verify(score).getFinalPunctuation();
    }

    @Test
    public void shouldReturnNSWhenChosenByNorthSouth() {
        String chosenByResponse = northChosenGameModeSummary.getChosenBy();

        assertEquals("N/S", chosenByResponse);
    }

    @Test
    public void shouldReturnEWWhenNotChosenByNorthSouth() {
        GameModeSummary eastChosenGameModeSummary = new GameModeSummary(Direction.EAST, ruleset, orderOfPlay, score);

        String chosenByResponse = eastChosenGameModeSummary.getChosenBy();

        assertEquals("E/W", chosenByResponse);
    }

    @Test
    public void shouldGetOrderOfPlayFromOrderOfPlay() {
        assertEquals(orderOfPlayString, northChosenGameModeSummary.getOrderOfPlay());
    }

}
