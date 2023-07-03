package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public class StrainTest {

    private static Strain diamonds;
    private static Strain clubs;
    private static Strain hearts;
    private static Strain spades;
    private static Strain noTrumps;

    @BeforeAll
    public static void setup() {
        diamonds = Strain.DIAMONDS;
        clubs = Strain.CLUBS;
        hearts = Strain.HEARTS;
        spades = Strain.SPADES;
        noTrumps = Strain.NOTRUMPS;
    }

    @Test
    public void theSameStrainShouldAlwaysBeTheSameObject() {
        Strain strain1 = Strain.CLUBS;
        Strain strain2 = Strain.CLUBS;
        assertTrue(strain1 == strain2);
    }

    @Test
    public void theSameStrainShouldAlwaysBeEqual() {
        Strain strain1 = Strain.CLUBS;
        Strain strain2 = Strain.CLUBS;
        assertEquals(strain1, strain2);
    }

    @Test
    public void shouldGetName() {
        assertEquals("Diamonds", diamonds.getName());
        assertEquals("Clubs", clubs.getName());
        assertEquals("Hearts", hearts.getName());
        assertEquals("Spades", spades.getName());
        assertEquals("No Trumps", noTrumps.getName());
    }

    @Test
    public void shouldGetPositiveRuleset() {
        assertEquals(new PositiveWithTrumpsRuleset(Suit.DIAMONDS), diamonds.getPositiveRuleset());
        assertEquals(new PositiveWithTrumpsRuleset(Suit.CLUBS), clubs.getPositiveRuleset());
        assertEquals(new PositiveWithTrumpsRuleset(Suit.HEARTS), hearts.getPositiveRuleset());
        assertEquals(new PositiveWithTrumpsRuleset(Suit.SPADES), spades.getPositiveRuleset());
        assertEquals(new PositiveNoTrumpsRuleset(), noTrumps.getPositiveRuleset());
    }

}
