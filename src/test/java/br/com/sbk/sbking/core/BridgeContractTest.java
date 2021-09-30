package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BridgeContractTest {

    private static Strain anyStrain = Strain.DIAMONDS;
    private static int anyLevel = 3;
    private static boolean doubled = true;
    private static boolean redoubled = false;

    private BridgeContract setup() {
        return new BridgeContract(anyLevel, anyStrain, doubled, redoubled, false, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidLowerLevel() {
        new BridgeContract(0, anyStrain, false, false, false, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidUpperLevel() {
        new BridgeContract(8, anyStrain, false, false, false, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidStrain() {
        new BridgeContract(7, null, false, false, false, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithDoubledAndRedoubled() {
        new BridgeContract(7, null, true, true, false, Direction.NORTH);
    }

    @Test
    public void shouldGetItsLevel() {
        BridgeContract subject = this.setup();
        assertEquals(anyLevel, subject.getLevel());
    }

    @Test
    public void shouldGetItsStrain() {
        BridgeContract subject = this.setup();
        assertEquals(anyStrain, subject.getStrain());
    }

    @Test
    public void shouldGetItsDoubled() {
        BridgeContract subject = this.setup();
        assertEquals(doubled, subject.getDoubled());
    }

    @Test
    public void shouldGetItsRedoubled() {
        BridgeContract subject = this.setup();
        assertEquals(redoubled, subject.getRedoubled());
    }

    @Test
    public void shouldReturnIfItIsPartScore() {
        BridgeContract diamondsPartScore = new BridgeContract(4, Strain.DIAMONDS, false, false, false, Direction.NORTH);
        BridgeContract heartsPartScore = new BridgeContract(3, Strain.HEARTS, false, false, false, Direction.NORTH);
        BridgeContract noTrumpsPartScore = new BridgeContract(2, Strain.NOTRUMPS, false, false, false, Direction.NORTH);
        assertTrue(diamondsPartScore.isPartScore());
        assertTrue(heartsPartScore.isPartScore());
        assertTrue(noTrumpsPartScore.isPartScore());
    }

    @Test
    public void shouldReturnIfItIsGame() {
        BridgeContract diamondsGame = new BridgeContract(5, Strain.DIAMONDS, false, false, false, Direction.NORTH);
        BridgeContract heartsGame = new BridgeContract(4, Strain.HEARTS, false, false, false, Direction.NORTH);
        BridgeContract noTrumpsGame = new BridgeContract(3, Strain.NOTRUMPS, false, false, false, Direction.NORTH);
        assertTrue(diamondsGame.isGame());
        assertTrue(heartsGame.isGame());
        assertTrue(noTrumpsGame.isGame());
    }

    @Test
    public void shouldReturnIfItIsPetitSlam() {
        BridgeContract diamondsPetitSlam = new BridgeContract(6, Strain.DIAMONDS, false, false, false, Direction.NORTH);
        assertTrue(diamondsPetitSlam.isPetitSlam());
    }

    @Test
    public void shouldReturnIfItIsGrandSlam() {
        BridgeContract diamondsGrandSlam = new BridgeContract(7, Strain.DIAMONDS, false, false, false, Direction.NORTH);
        assertTrue(diamondsGrandSlam.isGrandSlam());
    }

}
