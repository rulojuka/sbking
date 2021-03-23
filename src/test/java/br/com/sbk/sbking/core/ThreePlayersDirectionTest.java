package br.com.sbk.sbking.core;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.BeforeClass;

public class ThreePlayersDirectionTest {

    private static final String FOREHAND_COMPLETE_NAME = "Forehand";
    private static final String MIDDLEHAND_COMPLETE_NAME = "Middlehand";
    private static final String HINDHAND_COMPLETE_NAME = "Hindhand";

    private static final char FOREHAND_ABBREVIATION = 'F';
    private static final char MIDDLEHAND_ABBREVIATION = 'M';
    private static final char HINDHAND_ABBREVIATION = 'H';

    private static ThreePlayersDirection forehand;
    private static ThreePlayersDirection middlehand;
    private static ThreePlayersDirection hindhand;

    @BeforeClass
    public static void setup() {
        forehand = ThreePlayersDirection.FOREHAND;
        middlehand = ThreePlayersDirection.MIDDLEHAND;
        hindhand = ThreePlayersDirection.HINDHAND;
    }

    @Test
    public void theSameDirectionShouldAlwaysBeTheSameObject() {
        ThreePlayersDirection forehand1 = ThreePlayersDirection.FOREHAND;
        ThreePlayersDirection forehand2 = ThreePlayersDirection.FOREHAND;
        assertTrue(forehand1 == forehand2);
    }

    @Test
    public void theSameDirectionShouldAlwaysBeEqual() {
        ThreePlayersDirection forehand1 = ThreePlayersDirection.FOREHAND;
        ThreePlayersDirection forehand2 = ThreePlayersDirection.FOREHAND;
        assertEquals(forehand1, forehand2);
    }

    @Test
    public void shouldKnowItsDirection() {
        assertTrue(forehand.isForehand());
        assertFalse(forehand.isMiddlehand());

        assertTrue(middlehand.isMiddlehand());
        assertFalse(middlehand.isHindhand());

        assertTrue(hindhand.isHindhand());
        assertFalse(hindhand.isForehand());
    }
    
    @Test
    public void shouldKnowItsImmediateNext() {
        assertTrue(forehand.next() == middlehand);
        assertTrue(middlehand.next() == hindhand);
        assertTrue(hindhand.next() == forehand);
    }

    @Test
    public void shouldKnowItsNonImmediateNext() {
        assertTrue(forehand.next(1) == middlehand);
        assertTrue(forehand.next(2) == hindhand);
        assertTrue(forehand.next(3) == forehand);
    }

    @Test
    public void shouldGetCompleteName() {
        assertEquals(FOREHAND_COMPLETE_NAME, forehand.getCompleteName());
        assertEquals(MIDDLEHAND_COMPLETE_NAME, middlehand.getCompleteName());
        assertEquals(HINDHAND_COMPLETE_NAME, hindhand.getCompleteName());
    }

    @Test
    public void shouldGetAbbreviation() {
        assertEquals(FOREHAND_ABBREVIATION, forehand.getAbbreviation());
        assertEquals(MIDDLEHAND_ABBREVIATION, middlehand.getAbbreviation());
        assertEquals(HINDHAND_ABBREVIATION, hindhand.getAbbreviation());
    }
}
