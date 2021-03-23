package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class RankTest {

    private static Rank two;
    private static Rank three;
    private static Rank four;
    private static Rank five;
    private static Rank six;
    private static Rank seven;
    private static Rank eight;
    private static Rank nine;
    private static Rank ten;
    private static Rank jack;
    private static Rank queen;
    private static Rank king;
    private static Rank ace;

    @BeforeClass
    public static void setup() {
        two = Rank.TWO;
        three = Rank.THREE;
        four = Rank.FOUR;
        five = Rank.FIVE;
        six = Rank.SIX;
        seven = Rank.SEVEN;
        eight = Rank.EIGHT;
        nine = Rank.NINE;
        ten = Rank.TEN;
        jack = Rank.JACK;
        queen = Rank.QUEEN;
        king = Rank.KING;
        ace = Rank.ACE;
    }

    @Test
    public void theSameRankShouldAlwaysBeTheSameObject() {
        Rank rank1 = Rank.TWO;
        Rank rank2 = Rank.TWO;
        assertTrue(rank1 == rank2);
    }

    @Test
    public void theSameRankShouldAlwaysBeEqual() {
        Rank rank1 = Rank.TWO;
        Rank rank2 = Rank.TWO;
        assertEquals(rank1, rank2);
    }

    @Test
    public void shouldGetName() {
        assertEquals("Two", two.getName());
        assertEquals("Three", three.getName());
        assertEquals("Four", four.getName());
        assertEquals("Five", five.getName());
        assertEquals("Six", six.getName());
        assertEquals("Seven", seven.getName());
        assertEquals("Eight", eight.getName());
        assertEquals("Nine", nine.getName());
        assertEquals("Ten", ten.getName());
        assertEquals("Jack", jack.getName());
        assertEquals("Queen", queen.getName());
        assertEquals("King", king.getName());
        assertEquals("Ace", ace.getName());
    }

    @Test
    public void shouldGetSymbol() {
        assertEquals("2", two.getSymbol());
        assertEquals("3", three.getSymbol());
        assertEquals("4", four.getSymbol());
        assertEquals("5", five.getSymbol());
        assertEquals("6", six.getSymbol());
        assertEquals("7", seven.getSymbol());
        assertEquals("8", eight.getSymbol());
        assertEquals("9", nine.getSymbol());
        assertEquals("T", ten.getSymbol());
        assertEquals("J", jack.getSymbol());
        assertEquals("Q", queen.getSymbol());
        assertEquals("K", king.getSymbol());
        assertEquals("A", ace.getSymbol());
    }

    @Test
    public void shouldCompareCorrectly() {
        assertTrue(three.compareTo(three) == 0);
        assertTrue(three.compareTo(two) > 0);
        assertTrue(three.compareTo(four) < 0);
    }

}