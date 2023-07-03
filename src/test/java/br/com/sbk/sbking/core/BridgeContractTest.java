package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BridgeContractTest {

    private static Strain anyStrain = Strain.DIAMONDS;
    private static int anyLevel = 3;
    private static boolean doubled = true;
    private static boolean redoubled = false;

    private BridgeContract setup() {
        return new BridgeContract(anyLevel, anyStrain, doubled, redoubled);
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidLowerLevel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BridgeContract(0, anyStrain, false, false);
        });

    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidUpperLevel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BridgeContract(8, anyStrain, false, false);
        });
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidStrain() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BridgeContract(7, null, false, false);
        });
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithDoubledAndRedoubled() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BridgeContract(7, null, true, true);
        });
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
        BridgeContract diamondsPartScore = new BridgeContract(4, Strain.DIAMONDS, false, false);
        BridgeContract heartsPartScore = new BridgeContract(3, Strain.HEARTS, false, false);
        BridgeContract noTrumpsPartScore = new BridgeContract(2, Strain.NOTRUMPS, false, false);
        assertTrue(diamondsPartScore.isPartScore());
        assertTrue(heartsPartScore.isPartScore());
        assertTrue(noTrumpsPartScore.isPartScore());
    }

    @Test
    public void shouldReturnIfItIsGame() {
        BridgeContract diamondsGame = new BridgeContract(5, Strain.DIAMONDS, false, false);
        BridgeContract heartsGame = new BridgeContract(4, Strain.HEARTS, false, false);
        BridgeContract noTrumpsGame = new BridgeContract(3, Strain.NOTRUMPS, false, false);
        assertTrue(diamondsGame.isGame());
        assertTrue(heartsGame.isGame());
        assertTrue(noTrumpsGame.isGame());
    }

    @Test
    public void shouldReturnIfItIsPetitSlam() {
        BridgeContract diamondsPetitSlam = new BridgeContract(6, Strain.DIAMONDS, false, false);
        assertTrue(diamondsPetitSlam.isPetitSlam());
    }

    @Test
    public void shouldReturnIfItIsGrandSlam() {
        BridgeContract diamondsGrandSlam = new BridgeContract(7, Strain.DIAMONDS, false, false);
        assertTrue(diamondsGrandSlam.isGrandSlam());
    }

}
