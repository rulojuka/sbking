package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OddTricksTest {

    private static OddTricks ONE;
    private static OddTricks TWO;
    private static OddTricks THREE;
    private static OddTricks FOUR;
    private static OddTricks FIVE;
    private static OddTricks SIX;
    private static OddTricks SEVEN;

    @BeforeAll
    public static void setup() {
        ONE = OddTricks.ONE;
        TWO = OddTricks.TWO;
        THREE = OddTricks.THREE;
        FOUR = OddTricks.FOUR;
        FIVE = OddTricks.FIVE;
        SIX = OddTricks.SIX;
        SEVEN = OddTricks.SEVEN;
    }

    @Test
    public void allOddTricksFromOneToSevenShouldExist() {
        assertNotNull(ONE);
        assertNotNull(TWO);
        assertNotNull(THREE);
        assertNotNull(FOUR);
        assertNotNull(FIVE);
        assertNotNull(SIX);
        assertNotNull(SEVEN);
    }

    @Test
    public void shouldCompareCorrectly() {
        assertTrue(THREE.compareTo(THREE) == 0);
        assertTrue(THREE.compareTo(TWO) > 0);
        assertTrue(THREE.compareTo(FOUR) < 0);
    }

    @Test
    public void theSameOddTricksShouldAlwaysBeTheSameObject() {
        OddTricks oddTricksA = OddTricks.TWO;
        OddTricks oddTricksB = OddTricks.TWO;
        assertTrue(oddTricksA == oddTricksB);
    }

    @Test
    public void theSameOddTricksShouldAlwaysBeEqual() {
        OddTricks oddTricksA = OddTricks.TWO;
        OddTricks oddTricksB = OddTricks.TWO;
        assertEquals(oddTricksA, oddTricksB);
    }

    @Test
    public void shouldGetName() {
        assertEquals("One",ONE.getName());
    }
}
